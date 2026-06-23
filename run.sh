#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
RUN_DIR="$ROOT/.run"
BACKEND="$ROOT/backend"
mkdir -p "$RUN_DIR"

resolve_john_server_hosts() {
  if ! command -v tailscale >/dev/null 2>&1 || [[ ! -f "$BACKEND/.env" ]]; then
    return 0
  fi
  local ts_ip
  ts_ip=$(tailscale ip -4 john-server 2>/dev/null | head -1 || true)
  [[ -n "$ts_ip" ]] || return 0

  if grep -q '@john-server:' "$BACKEND/.env"; then
    sed -i.bak "s|@john-server:|@${ts_ip}:|g" "$BACKEND/.env"
    rm -f "$BACKEND/.env.bak"
  fi
  if grep -q 'redis://john-server:' "$BACKEND/.env"; then
    sed -i.bak "s|redis://john-server:|redis://${ts_ip}:|g" "$BACKEND/.env"
    rm -f "$BACKEND/.env.bak"
  fi
}

assert_local_test_db() {
  local db_url
  db_url=$(grep '^DATABASE_URL=' "$BACKEND/.env" | cut -d= -f2-)
  if [[ "$db_url" == postgresql* ]] && [[ "$db_url" != *"-test"* ]]; then
    echo "error: 本地开发必须连接 *-test 测试库，当前: $db_url" >&2
    echo "       请使用 backend/.env.example 或运行 ./scripts/bootstrap-test-env.sh" >&2
    exit 1
  fi
  if [[ "$db_url" == sqlite:* ]]; then
    echo "error: 本地开发已改为 john-server 测试库，请勿使用 SQLite。" >&2
    echo "       运行: cp backend/.env.example backend/.env && ./scripts/bootstrap-test-env.sh" >&2
    exit 1
  fi
}

start_backend() {
  cd "$BACKEND"
  if [[ ! -d .venv ]]; then
    python3 -m venv .venv
    source .venv/bin/activate
    pip install -r requirements.txt
  else
    source .venv/bin/activate
  fi
  if [[ ! -f .env ]]; then
    cp .env.example .env
  fi
  resolve_john_server_hosts
  assert_local_test_db
  echo "DATABASE_URL=$(grep '^DATABASE_URL=' .env | cut -d= -f2-)"
  echo "REDIS_URL=$(grep '^REDIS_URL=' .env | cut -d= -f2- || echo '(未配置)')"
  nohup uvicorn app.main:app --reload --host 0.0.0.0 --port 8004 > "$RUN_DIR/backend.log" 2>&1 &
  echo $! > "$RUN_DIR/backend.pid"
}

start_frontend() {
  cd "$ROOT/frontend"
  if [[ ! -d node_modules ]]; then
    npm install
  fi
  nohup npm run dev > "$RUN_DIR/frontend.log" 2>&1 &
  echo $! > "$RUN_DIR/frontend.pid"
}

stop_svc() {
  local name=$1
  local pid_file="$RUN_DIR/${name}.pid"
  if [[ -f "$pid_file" ]]; then
    kill "$(cat "$pid_file")" 2>/dev/null || true
    rm -f "$pid_file"
  fi
  if [[ "$name" == "backend" ]]; then
    lsof -ti tcp:8004 2>/dev/null | xargs kill 2>/dev/null || true
  fi
  if [[ "$name" == "frontend" ]]; then
    lsof -ti tcp:3004 2>/dev/null | xargs kill 2>/dev/null || true
  fi
}

case "${1:-start}" in
  start)
    start_backend
    start_frontend
    echo "John Algorithm started:"
    echo "  Web:     http://localhost:3004"
    echo "  API:     http://localhost:8004/docs"
    ;;
  stop)
    stop_svc backend
    stop_svc frontend
    ;;
  restart)
    "$0" stop
    sleep 1
    "$0" start
    ;;
  sync)
    cd "$BACKEND" && source .venv/bin/activate
    assert_local_test_db
    python3 "$ROOT/scripts/sync-problems.py"
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|sync}"
    exit 1
    ;;
esac
