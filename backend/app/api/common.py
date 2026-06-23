from __future__ import annotations

from fastapi import APIRouter

router = APIRouter(tags=["common"])


@router.get("/health")
def health():
    return {"status": "ok"}
