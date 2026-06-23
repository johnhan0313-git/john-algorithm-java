# Portainer 部署

## 1. 一次性：john-server 创建 PG 库

在**本机**（需能 ssh john-server）：

```bash
chmod +x scripts/init-john-server.sh scripts/bootstrap-prod-db.sh
./scripts/init-john-server.sh
```

会创建：

- 用户 `john-algorithm` / 密码 `john-algorithm-123`
- 生产库 `john-algorithm`
- 测试库 `john-algorithm-test`

## 2. Portainer Stack

- Stack 文件：[docker-compose.prod.yml](../docker-compose.prod.yml)
- Git 仓库拉取后 Build & Deploy
- 确认 external networks 存在：`john-postgresql_default`、`john-redis_default`

### Stack 环境变量（Portainer UI 填写）

| 变量 | 示例 | 必填 |
|------|------|------|
| `JWT_SECRET` | 随机长字符串 | 是 |
| `SYNC_API_KEY` | 随机字符串 | 是 |
| `SMTP_HOST` | smtp.163.com | 是（发验证码） |
| `SMTP_PORT` | 465 | 是 |
| `SMTP_USER` | your@163.com | 是 |
| `SMTP_PASSWORD` | 授权码 | 是 |
| `SMTP_FROM` | your@163.com | 是 |
| `CORS_ORIGINS` | `https://你的域名,http://服务器IP:3004` | 按需 |

`docker-compose.prod.yml` 已内置：

- `DATABASE_URL` → `john-algorithm`（生产库）
- `REDIS_URL` → `redis://john-redis:6379/2`
- `AUTH_EXPOSE_CODES=false`
- `JWT_EXPIRE_MINUTES=720`

容器启动时 **backend 自动执行 `alembic upgrade head` 建表**。

## 3. 首次部署后：同步题目到生产库

Stack 跑起来后，在**能访问 Docker 内网或 Tailscale PG** 的机器上执行一次：

```bash
# 方式 A：ssh 到 john-server，在仓库目录
cd john-algorithm-java/backend
source .venv/bin/activate
./../scripts/bootstrap-prod-db.sh

# 方式 B：本地 Tailscale 直连生产库（慎用，确认 URL 无 -test）
DATABASE_URL='postgresql+psycopg://john-algorithm:john-algorithm-123@john-server:5432/john-algorithm' \
  ./scripts/bootstrap-prod-db.sh
```

或仅同步（表已由容器 migration 建好）：

```bash
DATABASE_URL='postgresql+psycopg://john-algorithm:john-algorithm-123@john-server:5432/john-algorithm' \
  python3 scripts/sync-problems.py
```

## 4. 访问

| 服务 | 地址 |
|------|------|
| 前端 | `http://<john-server>:3004` |
| API 文档 | `http://<john-server>:8004/docs` |

## 5. 本地开发 vs 生产

| | 本地 `./run.sh` | Portainer 生产 |
|--|----------------|----------------|
| 数据库 | `john-algorithm-test` | `john-algorithm` |
| Redis | `john-server:6379/2` | `john-redis:6379/2` |
| 验证码 dev_code | `AUTH_EXPOSE_CODES=true` | `false` |

本地初始化测试库：

```bash
./scripts/bootstrap-test-env.sh
./run.sh start
```
