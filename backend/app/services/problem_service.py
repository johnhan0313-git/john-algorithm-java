from __future__ import annotations

import json

from sqlalchemy.orm import Session

from app.constants import CATEGORIES, DIFF_LABELS
from app.models.problem import Problem
from app.models.progress import ProblemProgress
from app.schemas.problem import (
    CategoryMeta,
    ProblemDetail,
    ProblemSummary,
    StatsResponse,
)
from app.services.cache import cache_get, cache_key, cache_set, invalidate_problems_cache
from app.utils.time import utc_now_ms


def upsert_problems(db: Session, items: list[dict]) -> int:
    count = 0
    for item in items:
        row = db.query(Problem).filter(Problem.slug == item["slug"]).first()
        if row is None:
            row = Problem(slug=item["slug"])
            db.add(row)
        for key, value in item.items():
            if key == "slug":
                continue
            setattr(row, key, value)
        row.synced_at = utc_now_ms()
        count += 1
    db.commit()
    invalidate_problems_cache()
    return count


def _done_slugs(db: Session, user_id: int) -> set[str]:
    rows = (
        db.query(Problem.slug)
        .join(ProblemProgress, ProblemProgress.problem_id == Problem.id)
        .filter(ProblemProgress.user_id == user_id, ProblemProgress.status == "done")
        .all()
    )
    return {r[0] for r in rows}


def problem_to_summary(p: Problem, done: bool = False) -> ProblemSummary:
    return ProblemSummary(
        id=p.slug,
        lc_num=p.lc_num,
        title=p.title,
        full_title=p.full_title,
        category=p.category_code,
        category_label=CATEGORIES.get(p.category_code, p.category_code),
        difficulty=p.difficulty,
        difficulty_label=DIFF_LABELS.get(p.difficulty, p.difficulty),
        frequency=p.frequency,
        freq_level=p.freq_level,
        companies=p.companies,
        pass_rate=p.pass_rate,
        pass_rate_text=p.pass_rate_text,
        fqn=p.solution_fqn,
        class_name=p.class_name,
        run_command=p.run_command,
        summary=p.summary,
        code_lines=p.code_lines,
        done=done,
    )


def problem_to_detail(p: Problem, done: bool = False) -> ProblemDetail:
    base = problem_to_summary(p, done=done)
    return ProblemDetail(
        **base.model_dump(),
        description=p.description,
        example=p.example,
        approach=p.approach,
        notes=p.notes,
        pitfalls=p.pitfalls,
        file_path=p.file_path,
        idea_path=p.idea_path,
        solution_code=p.solution_code,
    )


def list_problems(
    db: Session,
    *,
    user_id: int,
    category: str | None = None,
    difficulty: str | None = None,
    freq_level: str | None = None,
    q: str | None = None,
) -> tuple[list[ProblemSummary], int]:
    filters = {
        "category": category or "",
        "difficulty": difficulty or "",
        "freq_level": freq_level or "",
        "q": q or "",
    }
    key = cache_key("problems:list", filters)
    cached = cache_get(key)
    if cached:
        payload = json.loads(cached)
        items = [ProblemSummary(**item) for item in payload["items"]]
    else:
        query = db.query(Problem).order_by(Problem.category_code, Problem.difficulty, Problem.lc_num)
        if category:
            query = query.filter(Problem.category_code == category)
        if difficulty:
            query = query.filter(Problem.difficulty == difficulty)
        if freq_level:
            query = query.filter(Problem.freq_level == freq_level)
        if q:
            like = f"%{q.lower()}%"
            query = query.filter(
                Problem.title.ilike(like)
                | Problem.lc_num.ilike(like)
                | Problem.companies.ilike(like)
                | Problem.solution_fqn.ilike(like)
                | Problem.description.ilike(like)
            )
        rows = query.all()
        items = [problem_to_summary(p, done=False) for p in rows]
        cache_set(key, json.dumps({"items": [i.model_dump() for i in items], "total": len(items)}))

    done = _done_slugs(db, user_id)
    for item in items:
        item.done = item.id in done
    return items, len(items)


def get_problem(db: Session, slug: str, user_id: int) -> ProblemDetail | None:
    row = db.query(Problem).filter(Problem.slug == slug).first()
    if not row:
        return None
    done = (
        db.query(ProblemProgress)
        .filter(
            ProblemProgress.user_id == user_id,
            ProblemProgress.problem_id == row.id,
            ProblemProgress.status == "done",
        )
        .first()
        is not None
    )
    return problem_to_detail(row, done=done)


def compute_stats(db: Session) -> StatsResponse:
    key = cache_key("stats")
    cached = cache_get(key)
    if cached:
        return StatsResponse(**json.loads(cached))

    rows = db.query(Problem).all()
    by_difficulty: dict[str, int] = {}
    by_category: dict[str, int] = {}
    high_freq = 0
    for p in rows:
        by_difficulty[p.difficulty] = by_difficulty.get(p.difficulty, 0) + 1
        label = CATEGORIES.get(p.category_code, p.category_code)
        by_category[label] = by_category.get(label, 0) + 1
        if p.freq_level == "极高":
            high_freq += 1
    stats = StatsResponse(
        total=len(rows),
        high_freq=high_freq,
        by_difficulty=by_difficulty,
        by_category=by_category,
    )
    cache_set(key, stats.model_dump_json())
    return stats


def list_categories() -> list[CategoryMeta]:
    return [CategoryMeta(key=k, label=v) for k, v in CATEGORIES.items()]
