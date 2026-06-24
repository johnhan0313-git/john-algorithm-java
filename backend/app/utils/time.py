from __future__ import annotations

from datetime import datetime, timezone


def utc_now_ms() -> int:
    """UTC epoch milliseconds (Java System.currentTimeMillis() compatible)."""
    return int(datetime.now(timezone.utc).timestamp() * 1000)
