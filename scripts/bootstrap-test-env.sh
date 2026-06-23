#!/usr/bin/env bash
# 本地一次性：john-server 建库 + 迁移表结构 + 同步题目到测试库
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
BACKEND="$ROOT/backend"

echo "==> 1/3 john-server 创建 PG 用户与库（john-algorithm / john-algorithm-test）"
"$ROOT/scripts/init-john-server.sh"

if [[ ! -f "$BACKEND/.env" ]]; then
  cp "$BACKEND/.env.example" "$BACKEND/.env"
  echo "    已创建 backend/.env"
fi

# 若已有 .env 仍指向 sqlite，提示覆盖
if grep -q '^DATABASE_URL=sqlite:' "$BACKEND/.env" 2>/dev/null; then
  echo "==> 检测到 backend/.env 仍使用 SQLite，已按 .env.example 更新为测试库"
  cp "$BACKEND/.env.example" "$BACKEND/.env"
fi

cd "$BACKEND"
if [[ ! -d .venv ]]; then
  python3 -m venv .venv
fi
# shellcheck disable=SC1091
source .venv/bin/activate
pip install -q -r requirements.txt

db_url=$(grep '^DATABASE_URL=' .env | cut -d= -f2-)
if [[ "$db_url" != *"-test"* ]]; then
  echo "error: DATABASE_URL 必须指向 *-test 测试库，当前: $db_url" >&2
  exit 1
fi

echo "==> 2/3 Alembic 迁移（建表）"
echo "    DATABASE_URL=$db_url"
alembic upgrade head

echo "==> 3/3 同步 Java 题目到测试库"
python3 "$ROOT/scripts/sync-problems.py"

echo "==> 完成。启动: ./run.sh start"
