from __future__ import annotations

from sqlalchemy import JSON, BigInteger, Float, Integer, String, Text
from sqlalchemy.orm import Mapped, mapped_column

from app.database import Base
from app.utils.time import utc_now_ms


class Problem(Base):
    __tablename__ = "problems"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    slug: Mapped[str] = mapped_column(String(128), unique=True, index=True)
    type_code: Mapped[str] = mapped_column(String(32), default="leetcode", index=True)
    lc_num: Mapped[str] = mapped_column(String(16), index=True)
    title: Mapped[str] = mapped_column(String(256))
    full_title: Mapped[str] = mapped_column(String(512))
    difficulty: Mapped[str] = mapped_column(String(16), index=True)
    category_code: Mapped[str] = mapped_column(String(32), index=True)
    description: Mapped[str] = mapped_column(Text, default="")
    example: Mapped[str] = mapped_column(Text, default="")
    frequency: Mapped[str] = mapped_column(Text, default="")
    freq_level: Mapped[str] = mapped_column(String(16), default="中", index=True)
    companies: Mapped[str] = mapped_column(Text, default="")
    pass_rate: Mapped[float] = mapped_column(Float, default=0.0)
    pass_rate_text: Mapped[str] = mapped_column(String(64), default="")
    file_path: Mapped[str] = mapped_column(String(512), default="")
    idea_path: Mapped[str] = mapped_column(String(512), default="")
    solution_fqn: Mapped[str] = mapped_column(String(256), default="")
    class_name: Mapped[str] = mapped_column(String(128), default="")
    run_command: Mapped[str] = mapped_column(String(512), default="")
    summary: Mapped[str] = mapped_column(Text, default="")
    approach: Mapped[str] = mapped_column(Text, default="")
    notes: Mapped[str] = mapped_column(Text, default="")
    pitfalls: Mapped[str] = mapped_column(Text, default="")
    solution_code: Mapped[str] = mapped_column(Text, default="")
    code_lines: Mapped[int] = mapped_column(Integer, default=0)
    extra: Mapped[dict] = mapped_column(JSON, default=dict)
    synced_at: Mapped[int] = mapped_column(BigInteger, default=utc_now_ms, onupdate=utc_now_ms, nullable=False)
