#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"
python3 scripts/generate-ui-data.py
echo "UI: http://localhost:8765"
cd ui && python3 -m http.server 8765
