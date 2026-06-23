import os

os.environ.setdefault("TESTING", "1")
os.environ.setdefault("DATABASE_URL", "sqlite:///:memory:")
os.environ.setdefault("AUTH_EXPOSE_CODES", "true")
os.environ.setdefault("USE_MIGRATIONS", "true")
os.environ.setdefault("JWT_SECRET", "test-secret")

if os.environ["DATABASE_URL"].startswith("postgresql"):
    db_name = os.environ["DATABASE_URL"].rsplit("/", 1)[-1]
    if "-test" not in db_name:
        raise RuntimeError(f"Refusing tests on non-test database: {db_name}")

import pytest
from fastapi.testclient import TestClient

from app.config import get_settings
from app.database import init_db, reset_engine_for_tests
from app.main import app


@pytest.fixture(autouse=True)
def _fresh_db():
    get_settings.cache_clear()
    reset_engine_for_tests()
    init_db()
    yield
    reset_engine_for_tests()
    get_settings.cache_clear()


@pytest.fixture
def client() -> TestClient:
    with TestClient(app) as test_client:
        yield test_client
