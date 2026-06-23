#!/usr/bin/env bash
set -euo pipefail

HOST="${JOHN_SERVER_HOST:-john-server}"
PG_CONTAINER="${PG_CONTAINER:-john-postgresql}"
PG_ADMIN_USER="${PG_ADMIN_USER:-appuser}"
PG_ADMIN_DB="${PG_ADMIN_DB:-appdb}"
DB_USER="john-algorithm"
DB_PASS="john-algorithm-123"

echo "==> Creating PostgreSQL user and databases on ${HOST}..."

ssh "${HOST}" bash -s <<'REMOTE'
set -euo pipefail
PG_CONTAINER="${PG_CONTAINER:-john-postgresql}"
PG_ADMIN_USER="${PG_ADMIN_USER:-appuser}"
PG_ADMIN_DB="${PG_ADMIN_DB:-appdb}"
DB_USER="john-algorithm"
DB_PASS="john-algorithm-123"

psql_cmd() {
  docker exec "${PG_CONTAINER}" psql -U "${PG_ADMIN_USER}" -d "${PG_ADMIN_DB}" -c "$1"
}

psql_cmd "SELECT 1 FROM pg_roles WHERE rolname = '${DB_USER}'" | grep -q 1 || \
  psql_cmd "CREATE USER \"${DB_USER}\" WITH PASSWORD '${DB_PASS}';"

psql_cmd "SELECT 1 FROM pg_database WHERE datname = 'john-algorithm'" | grep -q 1 || \
  psql_cmd "CREATE DATABASE \"john-algorithm\" OWNER \"${DB_USER}\";"

psql_cmd "SELECT 1 FROM pg_database WHERE datname = 'john-algorithm-test'" | grep -q 1 || \
  psql_cmd "CREATE DATABASE \"john-algorithm-test\" OWNER \"${DB_USER}\";"

echo "PostgreSQL databases ready."
REMOTE

echo "==> Done."
echo "    Production DB: john-algorithm"
echo "    Test DB:       john-algorithm-test"
