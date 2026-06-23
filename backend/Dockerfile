FROM python:3.12-slim

WORKDIR /app

COPY requirements.txt .
# 构建阶段不走代理，使用国内 PyPI 镜像（john-server 经 mihomo 拉 PyPI 极慢）
RUN HTTP_PROXY= HTTPS_PROXY= http_proxy= https_proxy= \
    pip install --no-cache-dir --default-timeout=180 \
    -i https://pypi.tuna.tsinghua.edu.cn/simple \
    --trusted-host pypi.tuna.tsinghua.edu.cn \
    -r requirements.txt

COPY app ./app
COPY alembic ./alembic
COPY alembic.ini .
COPY docker-entrypoint.sh .
RUN chmod +x docker-entrypoint.sh

EXPOSE 8004

ENTRYPOINT ["./docker-entrypoint.sh"]
