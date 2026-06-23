#!/usr/bin/env bash
# 通过 rsync + docker compose 部署到 john-server，绕过 Portainer 从 GitHub clone 失败。
# 用法：./scripts/deploy-john-server.sh

set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
REMOTE="${JOHN_SERVER:-john-server}"
REMOTE_DIR="${REMOTE_DIR:-/home/john-han/apps/john-algorithm-java}"
ENV_FILE="${ENV_FILE:-.env.prod}"

echo "→ rsync to ${REMOTE}:${REMOTE_DIR}"
ssh "${REMOTE}" "mkdir -p '${REMOTE_DIR}'"
rsync -avz --delete \
  --exclude .git \
  --exclude node_modules \
  --exclude frontend/node_modules \
  --exclude frontend/dist \
  --exclude backend/.venv \
  --exclude backend/data \
  --exclude backend/.env \
  --exclude .env.prod \
  --exclude .run \
  --exclude '**/__pycache__' \
  --exclude '.cursor' \
  --exclude 'ui/data.js' \
  "${ROOT}/" "${REMOTE}:${REMOTE_DIR}/"

echo "→ docker compose up --build"
ssh "${REMOTE}" bash -s <<EOF
set -euo pipefail
cd "${REMOTE_DIR}"
if [[ ! -f "${ENV_FILE}" ]]; then
  echo "缺少 ${REMOTE_DIR}/${ENV_FILE}，请复制 .env.prod.example 并填写 JWT_SECRET、SMTP_* 等" >&2
  exit 1
fi
docker compose --env-file "${ENV_FILE}" -f docker-compose.prod.yml up -d --build
docker compose -f docker-compose.prod.yml ps
EOF

echo "✓ 部署完成"
echo "  前端: http://${REMOTE}:3004"
echo "  API:  http://${REMOTE}:8004/docs"
