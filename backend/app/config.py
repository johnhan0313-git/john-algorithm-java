from __future__ import annotations

from functools import lru_cache
from pathlib import Path

from pydantic_settings import BaseSettings, SettingsConfigDict

_BACKEND_DIR = Path(__file__).resolve().parents[1]
ENV_FILE = _BACKEND_DIR / ".env"


class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=str(ENV_FILE),
        env_file_encoding="utf-8",
        extra="ignore",
    )

    app_name: str = "John Algorithm"
    debug: bool = False
    testing: bool = False
    database_url: str = "sqlite:///./data/app.db"
    use_migrations: bool = True

    jwt_secret: str = "change-me-in-production"
    jwt_algorithm: str = "HS256"
    jwt_expire_minutes: int = 720

    smtp_host: str = ""
    smtp_port: int = 465
    smtp_user: str = ""
    smtp_password: str = ""
    smtp_from: str = ""
    smtp_use_tls: bool = False
    smtp_use_ssl: bool = True
    email_code_expire_minutes: int = 10
    email_code_cooldown_seconds: int = 60
    auth_expose_codes: bool = False

    redis_url: str = ""
    sync_api_key: str = "dev-sync-key"

    cors_origins: str = "http://localhost:3004,http://127.0.0.1:3004"

    @property
    def cors_origin_list(self) -> list[str]:
        return [o.strip() for o in self.cors_origins.split(",") if o.strip()]

    @property
    def smtp_configured(self) -> bool:
        return bool(self.smtp_host and (self.smtp_from or self.smtp_user))

    @property
    def redis_enabled(self) -> bool:
        return bool(self.redis_url.strip())


@lru_cache
def get_settings() -> Settings:
    return Settings()
