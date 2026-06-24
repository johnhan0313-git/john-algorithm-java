"""Store timestamps as UTC epoch milliseconds (BIGINT)."""

from typing import Sequence, Union

import sqlalchemy as sa
from alembic import op

revision: str = "003_timestamps_bigint"
down_revision: Union[str, None] = "002_normalize_timestamps_utc"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None

_MS = "1000"


def _epoch_ms_sql(column: str, tz: str) -> str:
    return f"(EXTRACT(EPOCH FROM ({column} AT TIME ZONE '{tz}')) * {_MS})::bigint"


def _drop_timestamp_defaults() -> None:
    for table, column in (
        ("users", "created_at"),
        ("problems", "synced_at"),
        ("problem_progress", "updated_at"),
    ):
        op.execute(f"ALTER TABLE {table} ALTER COLUMN {column} DROP DEFAULT")


def _upgrade_postgres() -> None:
    # After 002 all naive datetimes are UTC wall-clock values.
    _drop_timestamp_defaults()
    op.alter_column(
        "users",
        "created_at",
        existing_type=sa.DateTime(),
        type_=sa.BigInteger(),
        existing_nullable=False,
        postgresql_using=_epoch_ms_sql("created_at", "UTC"),
    )
    op.alter_column(
        "users",
        "last_login_at",
        existing_type=sa.DateTime(),
        type_=sa.BigInteger(),
        existing_nullable=True,
        postgresql_using=(
            f"CASE WHEN last_login_at IS NULL THEN NULL ELSE {_epoch_ms_sql('last_login_at', 'UTC')} END"
        ),
    )
    op.alter_column(
        "problems",
        "synced_at",
        existing_type=sa.DateTime(),
        type_=sa.BigInteger(),
        existing_nullable=False,
        postgresql_using=_epoch_ms_sql("synced_at", "UTC"),
    )
    op.alter_column(
        "problem_progress",
        "updated_at",
        existing_type=sa.DateTime(),
        type_=sa.BigInteger(),
        existing_nullable=False,
        postgresql_using=_epoch_ms_sql("updated_at", "UTC"),
    )


def _downgrade_postgres() -> None:
    op.alter_column(
        "users",
        "created_at",
        existing_type=sa.BigInteger(),
        type_=sa.DateTime(),
        existing_nullable=False,
        postgresql_using="to_timestamp(created_at / 1000.0) AT TIME ZONE 'UTC'",
    )
    op.alter_column(
        "users",
        "last_login_at",
        existing_type=sa.BigInteger(),
        type_=sa.DateTime(),
        existing_nullable=True,
        postgresql_using=(
            "CASE WHEN last_login_at IS NULL THEN NULL "
            "ELSE to_timestamp(last_login_at / 1000.0) AT TIME ZONE 'UTC' END"
        ),
    )
    op.alter_column(
        "problems",
        "synced_at",
        existing_type=sa.BigInteger(),
        type_=sa.DateTime(),
        existing_nullable=False,
        postgresql_using="to_timestamp(synced_at / 1000.0) AT TIME ZONE 'UTC'",
    )
    op.alter_column(
        "problem_progress",
        "updated_at",
        existing_type=sa.BigInteger(),
        type_=sa.DateTime(),
        existing_nullable=False,
        postgresql_using="to_timestamp(updated_at / 1000.0) AT TIME ZONE 'UTC'",
    )
    op.execute("ALTER TABLE users ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP")
    op.execute("ALTER TABLE problems ALTER COLUMN synced_at SET DEFAULT CURRENT_TIMESTAMP")
    op.execute("ALTER TABLE problem_progress ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP")


def _upgrade_sqlite() -> None:
    for table, column, nullable in (
        ("users", "created_at", False),
        ("users", "last_login_at", True),
        ("problems", "synced_at", False),
        ("problem_progress", "updated_at", False),
    ):
        with op.batch_alter_table(table) as batch_op:
            batch_op.alter_column(
                column,
                existing_type=sa.DateTime(),
                type_=sa.BigInteger(),
                existing_nullable=nullable,
            )


def _downgrade_sqlite() -> None:
    for table, column, nullable in (
        ("users", "created_at", False),
        ("users", "last_login_at", True),
        ("problems", "synced_at", False),
        ("problem_progress", "updated_at", False),
    ):
        with op.batch_alter_table(table) as batch_op:
            batch_op.alter_column(
                column,
                existing_type=sa.BigInteger(),
                type_=sa.DateTime(),
                existing_nullable=nullable,
            )


def upgrade() -> None:
    bind = op.get_bind()
    if bind.dialect.name == "postgresql":
        _upgrade_postgres()
    else:
        _upgrade_sqlite()


def downgrade() -> None:
    bind = op.get_bind()
    if bind.dialect.name == "postgresql":
        _downgrade_postgres()
    else:
        _downgrade_sqlite()
