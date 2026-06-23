#!/usr/bin/env bash
# 将 sf.cool-app.me 写入 john-server nginx（需已部署 algorithm 容器）
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
REMOTE="${JOHN_SERVER:-john-server}"
BLOCK_FILE="$ROOT/docs/nginx-sf.cool-app.me.conf"

scp "$BLOCK_FILE" "${REMOTE}:/tmp/nginx-sf-algorithm.conf"
ssh "${REMOTE}" bash -s <<'EOF'
set -euo pipefail
CONF="${HOME}/mydocker/mntdata/nginx/nginx.conf"
cp "$CONF" "${CONF}.bak.$(date +%Y%m%d%H%M%S)"
docker network connect john-algorithm-java_default john-nginx 2>/dev/null || true
python3 - <<'PY'
from pathlib import Path
conf = Path.home() / "mydocker/mntdata/nginx/nginx.conf"
block = Path("/tmp/nginx-sf-algorithm.conf").read_text(encoding="utf-8")
text = conf.read_text(encoding="utf-8")
marker = "    # 未配置的子域名"
if "sf.cool-app.me" in text:
    print("sf.cool-app.me already configured")
elif marker not in text:
    raise SystemExit("marker not found")
else:
    conf.write_text(text.replace(marker, block + "\n" + marker, 1), encoding="utf-8")
    print("nginx.conf updated")
PY
docker exec john-nginx nginx -t
docker restart john-nginx
EOF

echo "✓ https://sf.cool-app.me"
