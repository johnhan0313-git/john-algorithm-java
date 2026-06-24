from __future__ import annotations

import re

from pydantic import BaseModel, Field, field_validator


class UserResponse(BaseModel):
    id: int
    username: str
    email: str | None
    display_name: str | None
    created_at: int

    model_config = {"from_attributes": True}


class TokenResponse(BaseModel):
    access_token: str
    token_type: str = "bearer"
    user: UserResponse


class SendEmailCodeRequest(BaseModel):
    email: str = Field(min_length=3, max_length=128)

    @field_validator("email")
    @classmethod
    def validate_email(cls, v: str) -> str:
        normalized = v.strip().lower()
        if not re.match(r"^[^@\s]+@[^@\s]+\.[^@\s]+$", normalized):
            raise ValueError("Invalid email address")
        return normalized


class SendEmailCodeResponse(BaseModel):
    message: str = "Verification code sent"
    cooldown_seconds: int = 0
    dev_code: str | None = None


class EmailLoginRequest(BaseModel):
    email: str = Field(min_length=3, max_length=128)
    code: str = Field(min_length=4, max_length=8)

    @field_validator("email")
    @classmethod
    def validate_email(cls, v: str) -> str:
        normalized = v.strip().lower()
        if not re.match(r"^[^@\s]+@[^@\s]+\.[^@\s]+$", normalized):
            raise ValueError("Invalid email address")
        return normalized
