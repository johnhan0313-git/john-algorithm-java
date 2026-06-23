from __future__ import annotations

from pathlib import Path

from fastapi import APIRouter, Depends, Header, HTTPException
from pydantic import BaseModel
from sqlalchemy.orm import Session

from app.config import Settings, get_settings
from app.database import get_db
from app.services.problem_parser import scan_java_problems
from app.services.problem_service import upsert_problems

router = APIRouter(prefix="/admin", tags=["admin"])


class SyncResponse(BaseModel):
    synced: int


def _verify_sync_key(key: str | None, settings: Settings) -> None:
    if not key or key != settings.sync_api_key:
        raise HTTPException(status_code=401, detail="Invalid sync key")


@router.post("/sync", response_model=SyncResponse)
def sync_problems(
    db: Session = Depends(get_db),
    settings: Settings = Depends(get_settings),
    x_sync_key: str | None = Header(None, alias="X-Sync-Key"),
):
    _verify_sync_key(x_sync_key, settings)
    repo_root = Path(__file__).resolve().parents[3]
    items = scan_java_problems(repo_root)
    count = upsert_problems(db, items)
    return SyncResponse(synced=count)
