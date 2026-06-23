from __future__ import annotations

from fastapi.testclient import TestClient

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
