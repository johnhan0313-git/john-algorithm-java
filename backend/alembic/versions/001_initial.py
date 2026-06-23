"""Initial schema."""

from typing import Sequence, Union

import sqlalchemy as sa
from alembic import op

revision: str = "001_initial"
down_revision: Union[str, None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "users",
        sa.Column("id", sa.Integer(), autoincrement=True, nullable=False),
        sa.Column("username", sa.String(length=64), nullable=False),
        sa.Column("email", sa.String(length=128), nullable=True),
        sa.Column("display_name", sa.String(length=64), nullable=True),
        sa.Column("is_active", sa.Boolean(), nullable=False, server_default=sa.text("true")),
        sa.Column("last_login_at", sa.DateTime(), nullable=True),
        sa.Column("created_at", sa.DateTime(), server_default=sa.text("CURRENT_TIMESTAMP"), nullable=False),
        sa.PrimaryKeyConstraint("id"),
        sa.UniqueConstraint("email"),
        sa.UniqueConstraint("username"),
    )
    op.create_index("ix_users_username", "users", ["username"])

    op.create_table(
        "problems",
        sa.Column("id", sa.Integer(), autoincrement=True, nullable=False),
        sa.Column("slug", sa.String(length=128), nullable=False),
        sa.Column("type_code", sa.String(length=32), nullable=False, server_default="leetcode"),
        sa.Column("lc_num", sa.String(length=16), nullable=False),
        sa.Column("title", sa.String(length=256), nullable=False),
        sa.Column("full_title", sa.String(length=512), nullable=False),
        sa.Column("difficulty", sa.String(length=16), nullable=False),
        sa.Column("category_code", sa.String(length=32), nullable=False),
        sa.Column("description", sa.Text(), nullable=False, server_default=""),
        sa.Column("example", sa.Text(), nullable=False, server_default=""),
        sa.Column("frequency", sa.Text(), nullable=False, server_default=""),
        sa.Column("freq_level", sa.String(length=16), nullable=False, server_default="中"),
        sa.Column("companies", sa.Text(), nullable=False, server_default=""),
        sa.Column("pass_rate", sa.Float(), nullable=False, server_default="0"),
        sa.Column("pass_rate_text", sa.String(length=64), nullable=False, server_default=""),
        sa.Column("file_path", sa.String(length=512), nullable=False, server_default=""),
        sa.Column("idea_path", sa.String(length=512), nullable=False, server_default=""),
        sa.Column("solution_fqn", sa.String(length=256), nullable=False, server_default=""),
        sa.Column("class_name", sa.String(length=128), nullable=False, server_default=""),
        sa.Column("run_command", sa.String(length=512), nullable=False, server_default=""),
        sa.Column("summary", sa.Text(), nullable=False, server_default=""),
        sa.Column("approach", sa.Text(), nullable=False, server_default=""),
        sa.Column("notes", sa.Text(), nullable=False, server_default=""),
        sa.Column("pitfalls", sa.Text(), nullable=False, server_default=""),
        sa.Column("solution_code", sa.Text(), nullable=False, server_default=""),
        sa.Column("code_lines", sa.Integer(), nullable=False, server_default="0"),
        sa.Column("extra", sa.JSON(), nullable=False, server_default=sa.text("'{}'")),
        sa.Column("synced_at", sa.DateTime(), server_default=sa.text("CURRENT_TIMESTAMP"), nullable=False),
        sa.PrimaryKeyConstraint("id"),
        sa.UniqueConstraint("slug"),
    )
    op.create_index("ix_problems_slug", "problems", ["slug"])
    op.create_index("ix_problems_category_code", "problems", ["category_code"])
    op.create_index("ix_problems_difficulty", "problems", ["difficulty"])
    op.create_index("ix_problems_freq_level", "problems", ["freq_level"])
    op.create_index("ix_problems_lc_num", "problems", ["lc_num"])
    op.create_index("ix_problems_type_code", "problems", ["type_code"])

    op.create_table(
        "problem_progress",
        sa.Column("id", sa.Integer(), autoincrement=True, nullable=False),
        sa.Column("user_id", sa.Integer(), nullable=False),
        sa.Column("problem_id", sa.Integer(), nullable=False),
        sa.Column("status", sa.String(length=16), nullable=False, server_default="done"),
        sa.Column("notes", sa.Text(), nullable=False, server_default=""),
        sa.Column("updated_at", sa.DateTime(), server_default=sa.text("CURRENT_TIMESTAMP"), nullable=False),
        sa.ForeignKeyConstraint(["problem_id"], ["problems.id"], ondelete="CASCADE"),
        sa.ForeignKeyConstraint(["user_id"], ["users.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
        sa.UniqueConstraint("user_id", "problem_id", name="uq_user_problem"),
    )
    op.create_index("ix_problem_progress_user_id", "problem_progress", ["user_id"])
    op.create_index("ix_problem_progress_problem_id", "problem_progress", ["problem_id"])


def downgrade() -> None:
    op.drop_table("problem_progress")
    op.drop_table("problems")
    op.drop_table("users")
