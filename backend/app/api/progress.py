from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from app.auth.dependencies import get_current_user
from app.database import get_db
from app.models.problem import Problem
from app.models.progress import ProblemProgress
from app.models.user import User
from app.schemas.problem import ProgressItem, ProgressMapResponse, UpdateProgressRequest
from app.services.cache import invalidate_problems_cache

router = APIRouter(prefix="/progress", tags=["progress"])


@router.get("", response_model=ProgressMapResponse)
def get_progress(user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    rows = (
        db.query(ProblemProgress, Problem.slug)
        .join(Problem, Problem.id == ProblemProgress.problem_id)
        .filter(ProblemProgress.user_id == user.id)
        .all()
    )
    items = {
        slug: ProgressItem(
            slug=slug,
            status=progress.status,
            notes=progress.notes or "",
            updated_at=progress.updated_at.isoformat() if progress.updated_at else None,
        )
        for progress, slug in rows
    }
    return ProgressMapResponse(items=items)


@router.put("/{slug}", response_model=ProgressItem)
def upsert_progress(
    slug: str,
    body: UpdateProgressRequest,
    user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    problem = db.query(Problem).filter(Problem.slug == slug).first()
    if not problem:
        raise HTTPException(status_code=404, detail="Problem not found")

    row = (
        db.query(ProblemProgress)
        .filter(ProblemProgress.user_id == user.id, ProblemProgress.problem_id == problem.id)
        .first()
    )
    if body.status == "todo":
        if row:
            db.delete(row)
            db.commit()
        invalidate_problems_cache()
        return ProgressItem(slug=slug, status="todo", notes="")

    if row is None:
        row = ProblemProgress(user_id=user.id, problem_id=problem.id)
        db.add(row)
    row.status = body.status
    row.notes = body.notes
    db.commit()
    db.refresh(row)
    invalidate_problems_cache()
    return ProgressItem(
        slug=slug,
        status=row.status,
        notes=row.notes or "",
        updated_at=row.updated_at.isoformat() if row.updated_at else None,
    )
