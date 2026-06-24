from __future__ import annotations

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.auth.dependencies import get_current_user
from app.auth.email_codes import can_send_code, create_email_code, rollback_email_code, verify_email_code
from app.auth.email_service import EmailDeliveryError, send_login_code
from app.auth.jwt import create_access_token
from app.auth.users import get_or_create_user_by_email, normalize_email
from app.config import Settings, get_settings
from app.database import get_db
from app.models.user import User
from app.schemas.auth import (
    EmailLoginRequest,
    SendEmailCodeRequest,
    SendEmailCodeResponse,
    TokenResponse,
    UserResponse,
)
from app.utils.time import utc_now_ms

router = APIRouter(prefix="/auth", tags=["auth"])


def _user_response(user: User) -> UserResponse:
    display = user.display_name or user.username
    return UserResponse(
        id=user.id,
        username=user.username,
        email=user.email,
        display_name=display,
        created_at=user.created_at,
    )


def _issue_token(user: User, db: Session) -> TokenResponse:
    user.last_login_at = utc_now_ms()
    db.commit()
    db.refresh(user)
    return TokenResponse(
        access_token=create_access_token(user.id),
        user=_user_response(user),
    )


def _should_expose_dev_secrets(settings: Settings) -> bool:
    return settings.testing or settings.debug or settings.auth_expose_codes


@router.post("/email/send-code", response_model=SendEmailCodeResponse)
def send_email_code_endpoint(
    body: SendEmailCodeRequest,
    settings: Settings = Depends(get_settings),
):
    email = normalize_email(body.email)
    if not settings.testing:
        allowed, wait_seconds = can_send_code(email, cooldown_seconds=settings.email_code_cooldown_seconds)
        if not allowed:
            raise HTTPException(status_code=429, detail=f"请等待 {wait_seconds}s 后再试")

    code = create_email_code(email, ttl_seconds=settings.email_code_expire_minutes * 60)
    try:
        send_login_code(settings, email, code)
    except EmailDeliveryError as exc:
        rollback_email_code(email)
        raise HTTPException(status_code=503, detail=str(exc)) from exc

    return SendEmailCodeResponse(
        cooldown_seconds=settings.email_code_cooldown_seconds,
        dev_code=code if _should_expose_dev_secrets(settings) else None,
    )


@router.post("/email/login", response_model=TokenResponse)
def email_login(body: EmailLoginRequest, db: Session = Depends(get_db)):
    email = normalize_email(body.email)
    if not verify_email_code(email, body.code):
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="邮箱验证码错误或已过期")

    user = get_or_create_user_by_email(db, email)
    if not user.is_active:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Account inactive")
    return _issue_token(user, db)


@router.get("/me", response_model=UserResponse)
def me(user: User = Depends(get_current_user)):
    return _user_response(user)
