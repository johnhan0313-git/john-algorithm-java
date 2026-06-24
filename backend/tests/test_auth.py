from __future__ import annotations

from fastapi.testclient import TestClient

from app.database import SessionLocal
from app.models.user import User
from app.utils.time import utc_now_ms
from tests.auth_helpers import auth_headers, login_user


def test_email_login_and_me(client: TestClient):
    reg = login_user(client, email="alice@example.com")
    assert reg["token_type"] == "bearer"
    assert reg["user"]["email"] == "alice@example.com"

    me = client.get("/api/auth/me", headers=auth_headers(reg["access_token"]))
    assert me.status_code == 200
    assert me.json()["email"] == "alice@example.com"


def test_email_login_auto_register(client: TestClient):
    first = login_user(client, email="bob@example.com")
    again = login_user(client, email="bob@example.com")
    assert again["user"]["id"] == first["user"]["id"]


def test_wrong_email_code(client: TestClient):
    send = client.post("/api/auth/email/send-code", json={"email": "wrong@example.com"})
    assert send.status_code == 200
    resp = client.post(
        "/api/auth/email/login",
        json={"email": "wrong@example.com", "code": "000000"},
    )
    assert resp.status_code == 401


def test_problems_require_auth(client: TestClient):
    resp = client.get("/api/problems")
    assert resp.status_code == 401


def test_login_timestamps_are_epoch_millis(client: TestClient):
    before = utc_now_ms()
    reg = login_user(client, email="tz@example.com")
    after = utc_now_ms()

    with SessionLocal() as db:
        user = db.query(User).filter(User.email == "tz@example.com").one()
        assert user.last_login_at is not None
        assert before <= user.last_login_at <= after
        assert before <= user.created_at <= after
        assert abs(user.last_login_at - user.created_at) < 5000

    me = client.get("/api/auth/me", headers=auth_headers(reg["access_token"]))
    assert isinstance(me.json()["created_at"], int)
