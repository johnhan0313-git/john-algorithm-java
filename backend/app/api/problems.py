from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session

from app.auth.dependencies import get_current_user
from app.database import get_db
from app.models.user import User
from app.schemas.problem import ProblemDetail, ProblemsListResponse, StatsResponse
from app.services.problem_service import compute_stats, get_problem, list_categories, list_problems

router = APIRouter(prefix="/problems", tags=["problems"])


@router.get("", response_model=ProblemsListResponse)
def get_problems(
    category: str | None = None,
    difficulty: str | None = None,
    freq_level: str | None = Query(None, alias="freqLevel"),
    q: str | None = None,
    user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    items, total = list_problems(
        db,
        user_id=user.id,
        category=category,
        difficulty=difficulty,
        freq_level=freq_level,
        q=q,
    )
    return ProblemsListResponse(items=items, total=total, categories=list_categories())


@router.get("/stats/summary", response_model=StatsResponse)
def get_stats(user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    return compute_stats(db)


@router.get("/{slug}", response_model=ProblemDetail)
def get_problem_detail(
    slug: str,
    user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    detail = get_problem(db, slug, user.id)
    if not detail:
        raise HTTPException(status_code=404, detail="Problem not found")
    return detail
