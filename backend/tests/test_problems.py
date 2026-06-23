from __future__ import annotations

import sys
from pathlib import Path

from fastapi.testclient import TestClient

ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "backend"))

from app.database import SessionLocal
from app.services.problem_parser import scan_java_problems
from app.services.problem_service import upsert_problems
from tests.auth_helpers import auth_headers, login_user


def _seed_problems() -> None:
    items = scan_java_problems(ROOT)
    db = SessionLocal()
    try:
        upsert_problems(db, items[:3])
    finally:
        db.close()


def test_list_problems_after_login(client: TestClient):
    _seed_problems()
    auth = login_user(client)
    resp = client.get("/api/problems", headers=auth_headers(auth["access_token"]))
    assert resp.status_code == 200
    data = resp.json()
    assert data["total"] >= 1
    assert len(data["items"]) >= 1


def test_progress_toggle(client: TestClient):
    _seed_problems()
    auth = login_user(client)
    headers = auth_headers(auth["access_token"])
    list_resp = client.get("/api/problems", headers=headers)
    slug = list_resp.json()["items"][0]["id"]

    done = client.put(f"/api/progress/{slug}", headers=headers, json={"status": "done"})
    assert done.status_code == 200
    assert done.json()["status"] == "done"

    progress = client.get("/api/progress", headers=headers)
    assert progress.json()["items"][slug]["status"] == "done"

    todo = client.put(f"/api/progress/{slug}", headers=headers, json={"status": "todo"})
    assert todo.status_code == 200
    assert todo.json()["status"] == "todo"
