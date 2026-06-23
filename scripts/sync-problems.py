#!/usr/bin/env python3
"""从 Java 解法类同步题目到 PostgreSQL。"""

from __future__ import annotations

import argparse
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
BACKEND = ROOT / "backend"
sys.path.insert(0, str(BACKEND))

from app.database import SessionLocal, init_db  # noqa: E402
from app.services.problem_parser import scan_java_problems  # noqa: E402
from app.services.problem_service import upsert_problems  # noqa: E402


def main() -> None:
    parser = argparse.ArgumentParser(description="Sync Java problems into database")
    parser.add_argument("--database-url", help="Override DATABASE_URL")
    args = parser.parse_args()

    import os

    os.chdir(BACKEND)
    if args.database_url:
        os.environ["DATABASE_URL"] = args.database_url

    init_db()
    items = scan_java_problems(ROOT)
    db = SessionLocal()
    try:
        count = upsert_problems(db, items)
    finally:
        db.close()
    print(f"Synced {count} problems")


if __name__ == "__main__":
    main()
