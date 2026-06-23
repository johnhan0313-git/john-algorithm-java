#!/usr/bin/env bash
# 生产库：仅迁移 + 同步（Portainer 首次部署后于 john-server 或本地 Tailscale 执行）
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
BACKEND="$ROOT/backend"

DATABASE_URL="${DATABASE_URL:-postgresql+psycopg://john-algorithm:john-algorithm-123@john-postgresql:5432/john-algorithm}"

if [[ "$DATABASE_URL" == *"-test"* ]]; then
  echo "error: 此脚本用于生产库 john-algorithm，当前 URL 含 -test" >&2
  exit 1
fi

cd "$BACKEND"
# shellcheck disable=SC1091
source .venv/bin/activate 2>/dev/null || { python3 -m venv .venv && source .venv/bin/activate && pip install -q -r requirements.txt; }

export DATABASE_URL
echo "==> Alembic migrate: $DATABASE_URL"
alembic upgrade head

echo "==> Sync problems"
python3 "$ROOT/scripts/sync-problems.py"

echo "==> Done."
