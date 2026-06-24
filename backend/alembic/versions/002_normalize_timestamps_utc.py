"""Normalize naive timestamps to UTC storage."""

from typing import Sequence, Union

from alembic import op

revision: str = "002_normalize_timestamps_utc"
down_revision: Union[str, None] = "001_initial"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None

_LOCAL_NAIVE_COLUMNS = (
    ("users", "created_at"),
    ("problems", "synced_at"),
    ("problem_progress", "updated_at"),
)


def upgrade() -> None:
    bind = op.get_bind()
    if bind.dialect.name != "postgresql":
        return

    for table, column in _LOCAL_NAIVE_COLUMNS:
        op.execute(
            f"""
            UPDATE {table}
            SET {column} = ({column} AT TIME ZONE 'Asia/Shanghai') AT TIME ZONE 'UTC'
            """
        )


def downgrade() -> None:
    bind = op.get_bind()
    if bind.dialect.name != "postgresql":
        return

    for table, column in _LOCAL_NAIVE_COLUMNS:
        op.execute(
            f"""
            UPDATE {table}
            SET {column} = ({column} AT TIME ZONE 'UTC') AT TIME ZONE 'Asia/Shanghai'
            """
        )
