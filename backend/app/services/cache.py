from __future__ import annotations

import hashlib
import json
import logging
from typing import Any

import redis

from app.config import get_settings

logger = logging.getLogger(__name__)

_client: redis.Redis | None = None
CACHE_TTL = 600
CACHE_PREFIX = "algo:"


def get_redis() -> redis.Redis | None:
    global _client
    settings = get_settings()
    if not settings.redis_enabled:
        return None
    if _client is None:
        _client = redis.from_url(settings.redis_url, decode_responses=True)
    return _client


def cache_key(prefix: str, payload: dict[str, Any] | None = None) -> str:
    if not payload:
        return f"{CACHE_PREFIX}{prefix}"
    raw = json.dumps(payload, sort_keys=True, ensure_ascii=False)
    digest = hashlib.md5(raw.encode()).hexdigest()[:12]
    return f"{CACHE_PREFIX}{prefix}:{digest}"


def cache_get(key: str) -> str | None:
    client = get_redis()
    if not client:
        return None
    try:
        return client.get(key)
    except redis.RedisError:
        logger.warning("Redis get failed for %s", key)
        return None


def cache_set(key: str, value: str, ttl: int = CACHE_TTL) -> None:
    client = get_redis()
    if not client:
        return
    try:
        client.setex(key, ttl, value)
    except redis.RedisError:
        logger.warning("Redis set failed for %s", key)


def invalidate_problems_cache() -> None:
    client = get_redis()
    if not client:
        return
    try:
        for key in client.scan_iter(f"{CACHE_PREFIX}problems:*"):
            client.delete(key)
        client.delete(f"{CACHE_PREFIX}stats")
    except redis.RedisError:
        logger.warning("Redis invalidate failed")
