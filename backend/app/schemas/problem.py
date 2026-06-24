from __future__ import annotations

from pydantic import BaseModel, Field


class ProblemSummary(BaseModel):
    id: str
    lc_num: str
    title: str
    full_title: str
    category: str
    category_label: str
    difficulty: str
    difficulty_label: str
    frequency: str
    freq_level: str
    companies: str
    pass_rate: float
    pass_rate_text: str
    fqn: str
    class_name: str
    run_command: str
    summary: str
    code_lines: int
    done: bool = False


class ProblemDetail(ProblemSummary):
    description: str
    example: str
    approach: str
    notes: str
    pitfalls: str
    file_path: str
    idea_path: str
    solution_code: str


class CategoryMeta(BaseModel):
    key: str
    label: str


class StatsResponse(BaseModel):
    total: int
    high_freq: int
    by_difficulty: dict[str, int]
    by_category: dict[str, int]


class ProblemsListResponse(BaseModel):
    items: list[ProblemSummary]
    total: int
    categories: list[CategoryMeta]


class ProgressItem(BaseModel):
    slug: str
    status: str
    notes: str = ""
    updated_at: int | None = None


class ProgressMapResponse(BaseModel):
    items: dict[str, ProgressItem]


class UpdateProgressRequest(BaseModel):
    status: str = Field(pattern="^(todo|done)$")
    notes: str = ""
