# john-server 生产部署

仓库：<https://github.com/johnhan0313-git/john-algorithm-java>

## 部署方式选择

| 方式 | 适用 | 稳定性 |
|------|------|--------|
| **SSH + deploy 脚本（推荐）** | 日常发版、改环境变量 | 高，不依赖服务器访问 GitHub |
| Portainer Git Stack | 偶尔 Pull and redeploy | **低**，john-server 到 GitHub 经常 EOF / 超时 |

Portainer Git Stack 常见问题：

| 现象 | 原因 | 处理 |
|------|------|------|
| `clone ... EOF` | 代理/TLS 不稳定 | 重试 Pull，或改用 deploy 脚本 |
| **界面一直转圈、无容器** | **Git 已成功，卡在 `docker build` 的 pip/npm** | 见下 |

**build 很慢**：Portainer 构建时 pip 若走 mihomo 代理访问 PyPI，速度仅 ~15 KB/s，backend 镜像可能要 **20–40 分钟**。本项目 Dockerfile 已改为**国内 PyPI / npm 镜像且构建时禁用代理**，更新代码后重新 Deploy 会快很多。

部署过程中可在 john-server 上看进度：

```bash
docker logs -f portainer 2>&1 | tail -20
```

---

## 推荐：SSH 部署脚本

### 1. john-server 首次准备

```bash
# 本机：创建 PG 库（若尚未执行）
./scripts/init-john-server.sh

# 服务器：创建配置目录与密钥文件（不要提交 git）
ssh john-server 'mkdir -p ~/apps/john-algorithm-java'
scp .env.prod.example john-server:~/apps/john-algorithm-java/.env.prod
# ssh 上去编辑 .env.prod，填入 JWT_SECRET、SYNC_API_KEY、SMTP_* 等
```

生产库表结构与题目若尚未初始化（可选，容器 migration 也会建表）：

```bash
DATABASE_URL='postgresql+psycopg://john-algorithm:john-algorithm-123@john-server:5432/john-algorithm' \
  ./scripts/bootstrap-prod-db.sh
```

### 2. 本机发版

```bash
git push
chmod +x scripts/deploy-john-server.sh
./scripts/deploy-john-server.sh
```

脚本会 rsync 代码到 `~/apps/john-algorithm-java`，在服务器上 `docker compose -f docker-compose.prod.yml up -d --build`。

仅改环境变量时，编辑服务器 `~/apps/john-algorithm-java/.env.prod` 后再跑一遍脚本即可。

---

## 备选：Portainer Git Stack

网络通畅时可用；若 Pull 失败但容器仍在跑，**不要反复点 Pull**，改用上面的 SSH 脚本。

### 让 Portainer 走 mihomo 代理

john-server 上 mihomo 监听 `7890`（HTTP）。Portainer 需配置：

```yaml
# ~/portainer/portainer-compose.yaml
extra_hosts:
  - "host.docker.internal:host-gateway"
environment:
  HTTP_PROXY: http://host.docker.internal:7890
  HTTPS_PROXY: http://host.docker.internal:7890
  NO_PROXY: localhost,127.0.0.1,192.168.0.0/16,10.0.0.0/8,172.16.0.0/12
```

修改后 `docker compose up -d` 重启 Portainer。**mihomo 必须保持运行**。

### Portainer 步骤

1. Stacks → Add stack → **Git repository**
2. URL：`https://github.com/johnhan0313-git/john-algorithm-java.git`
3. Compose path：`docker-compose.prod.yml`
4. 环境变量：`JWT_SECRET`、`SYNC_API_KEY`、`SMTP_*`、`CORS_ORIGINS`
5. Deploy

---

## 环境变量

| 变量 | 生产值 | 必填 |
|------|--------|------|
| `JWT_SECRET` | 随机长字符串 | 是 |
| `SYNC_API_KEY` | 随机字符串 | 是 |
| `SMTP_HOST` / `SMTP_USER` / `SMTP_PASSWORD` / `SMTP_FROM` | 163 等 | 是 |
| `CORS_ORIGINS` | 前端访问域名 | 按需 |

`docker-compose.prod.yml` 已内置：

- `DATABASE_URL` → `john-postgresql:5432/john-algorithm`
- `REDIS_URL` → `redis://john-redis:6379/2`
- `JWT_EXPIRE_MINUTES=720`
- `AUTH_EXPOSE_CODES=false`

---

## 访问

| 服务 | 地址 |
|------|------|
| 前端 | `http://<john-server>:3004` |
| API 文档 | `http://<john-server>:8004/docs` |

## 本地开发 vs 生产

| | 本地 `./run.sh` | 生产 |
|--|----------------|------|
| 数据库 | `john-algorithm-test` | `john-algorithm` |
| 部署 | `./scripts/bootstrap-test-env.sh` | `./scripts/deploy-john-server.sh` |
