from __future__ import annotations

import logging
from collections.abc import Generator
from pathlib import Path

from sqlalchemy import create_engine, event, inspect, text
from sqlalchemy.engine import Engine
from sqlalchemy.orm import DeclarativeBase, Session, sessionmaker
from sqlalchemy.pool import StaticPool

from app.config import get_settings

logger = logging.getLogger(__name__)

_engine: Engine | None = None
_SessionLocal: sessionmaker[Session] | None = None


class Base(DeclarativeBase):
    pass


def _sqlite_on_connect(dbapi_connection, _connection_record) -> None:
    cursor = dbapi_connection.cursor()
    cursor.execute("PRAGMA journal_mode=WAL")
    cursor.execute("PRAGMA synchronous=NORMAL")
    cursor.execute("PRAGMA busy_timeout=30000")
    cursor.close()


def ensure_engine() -> Engine:
    global _engine, _SessionLocal
    settings = get_settings()
    url = settings.database_url
    if _engine is not None and str(_engine.url) == url:
        return _engine

    if _engine is not None:
        _engine.dispose()
        _engine = None
        _SessionLocal = None

    connect_args: dict[str, object] = {}
    engine_kwargs: dict[str, object] = {}
    if url.startswith("sqlite"):
        connect_args["check_same_thread"] = False
        connect_args["timeout"] = 30
        if ":memory:" in url:
            engine_kwargs["poolclass"] = StaticPool
    elif url.startswith("postgresql"):
        engine_kwargs["pool_pre_ping"] = True
        engine_kwargs["pool_size"] = 5
        engine_kwargs["max_overflow"] = 10

    _engine = create_engine(url, connect_args=connect_args, **engine_kwargs)
    if url.startswith("sqlite"):
        event.listen(_engine, "connect", _sqlite_on_connect)
    _SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=_engine)
    return _engine


def get_engine() -> Engine:
    return ensure_engine()


def SessionLocal() -> Session:
    ensure_engine()
    assert _SessionLocal is not None
    return _SessionLocal()


def reset_engine_for_tests() -> None:
    global _engine, _SessionLocal
    if _engine is not None:
        _engine.dispose()
    _engine = None
    _SessionLocal = None


def get_db() -> Generator[Session, None, None]:
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


def run_migrations() -> None:
    from alembic import command
    from alembic.config import Config

    backend_dir = Path(__file__).resolve().parents[1]
    cfg = Config(str(backend_dir / "alembic.ini"))
    command.upgrade(cfg, "head")
    logger.info("Database migrations applied")


def reset_test_database() -> None:
    settings = get_settings()
    engine = get_engine()
    if settings.database_url.startswith("postgresql"):
        insp = inspect(engine)
        tables = [t for t in insp.get_table_names() if t != "alembic_version"]
        if not tables:
            return
        quoted = ", ".join(f'"{t}"' for t in tables)
        with engine.connect().execution_options(isolation_level="AUTOCOMMIT") as conn:
            conn.execute(text(f"TRUNCATE {quoted} RESTART IDENTITY CASCADE"))
        return
    Base.metadata.drop_all(bind=engine)
    Base.metadata.create_all(bind=engine)


def init_db() -> None:
    settings = get_settings()
    if settings.database_url.startswith("sqlite"):
        db_path = settings.database_url.replace("sqlite:///", "")
        if db_path and not db_path.startswith(":"):
            Path(db_path).parent.mkdir(parents=True, exist_ok=True)

    from app import models  # noqa: F401

    engine = get_engine()
    insp = inspect(engine)
    if settings.testing:
        if not insp.has_table("users"):
            if settings.use_migrations or settings.database_url.startswith("postgresql"):
                run_migrations()
            else:
                Base.metadata.create_all(bind=engine)
        reset_test_database()
        return

    if settings.use_migrations or insp.has_table("alembic_version"):
        run_migrations()
        return

    Base.metadata.create_all(bind=engine)
