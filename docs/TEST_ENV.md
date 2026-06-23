# 本地测试环境（PostgreSQL + Redis）

本地 `./run.sh start` 启动前后端；数据库连接 john-server 上与生产 **1:1** 的测试资源（`-test` 后缀），通过 **Tailscale** 访问。

## 架构

```
本地 Mac/PC                    john-server (Tailscale)
┌─────────────────┐           ┌──────────────────────────┐
│ run.sh          │           │ john-postgresql :5432    │
│  frontend :3004 │           │   └─ john-algorithm-test │
│  backend  :8004 │──Tailscale│ john-redis :6379/2       │
└─────────────────┘           └──────────────────────────┘
```

| 资源 | 生产 | 本地测试 |
|------|------|----------|
| PostgreSQL 库 | `john-algorithm` | `john-algorithm-test` |
| Redis DB | `2` | `2` |

## 前置条件

1. 本机已加入 Tailscale，能 ssh `john-server`
2. john-server 上 PG **5432** 对 Tailscale 可达

```bash
nc -zv john-server 5432
```

## john-server 一次性初始化

```bash
chmod +x scripts/init-john-server.sh run.sh
./scripts/init-john-server.sh
```

## 本地启动

```bash
cp backend/.env.example backend/.env   # 首次；默认已是 john-algorithm-test
./scripts/bootstrap-test-env.sh        # 首次：建库 + 建表 + 同步题目
./run.sh start
```

`run.sh` 会校验：本地 **禁止** SQLite 与生产库 `john-algorithm`（无 `-test` 后缀）。

- 前端：http://localhost:3004
- 后端：http://localhost:8004/docs
- 日志：`.run/backend.log`、`.run/frontend.log`

## 环境变量要点

| 变量 | 本地测试值 |
|------|-----------|
| `DATABASE_URL` | `postgresql+psycopg://john-algorithm:...@john-server:5432/john-algorithm-test` |
| `REDIS_URL` | `redis://john-server:6379/2`（可选） |
| `AUTH_EXPOSE_CODES` | `true`（本地免 SMTP） |
| `JWT_EXPIRE_MINUTES` | `720` |

## 跑测试

```bash
cd backend
python3 -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
pytest
```

pytest 默认使用内存 SQLite；若指向 PostgreSQL，库名必须含 `-test`。

## 与生产隔离

- **禁止** 本地 `.env` 指向 `john-algorithm` 生产库
- 生产 Stack 使用 `docker-compose.prod.yml`
