# syntax=docker/dockerfile:1

FROM node:22-alpine AS build
ARG GH_PACKAGES_TOKEN
ARG GITHUB_TOKEN
ARG NPM_REGISTRY=https://registry.npmmirror.com
WORKDIR /app
COPY package.json package-lock.json* ./
RUN --mount=type=secret,id=gh_packages_token,required=false \
    TOKEN="$(cat /run/secrets/gh_packages_token 2>/dev/null || true)" \
    && TOKEN="${TOKEN:-${GH_PACKAGES_TOKEN:-${GITHUB_TOKEN}}}" \
    && test -n "$TOKEN" || (echo "GH_PACKAGES_TOKEN required: Portainer 挂载 /home/john-han/.secrets -> /run/john-secrets，或 deploy 脚本 / export" >&2 && exit 1) \
    && printf '@johnhan0313-git:registry=https://npm.pkg.github.com\n//npm.pkg.github.com/:_authToken=%s\nalways-auth=true\n' "$TOKEN" > .npmrc \
    && npm config set registry "${NPM_REGISTRY}" \
    && if [ -f package-lock.json ]; then npm ci; else npm install; fi
COPY . .
RUN npm run build

FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
