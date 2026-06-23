from __future__ import annotations

import re
import secrets

from sqlalchemy.orm import Session

from app.models.user import User


def normalize_email(email: str) -> str:
    return email.strip().lower()


def _username_from_email(email: str) -> str:
    local = email.split("@", 1)[0]
    base = re.sub(r"[^a-zA-Z0-9_]", "_", local).strip("_")[:24] or "user"
    return f"{base}_{secrets.token_hex(3)}"


def _unique_username(db: Session, email: str) -> str:
    for _ in range(8):
        candidate = _username_from_email(email)
        exists = db.query(User.id).filter(User.username == candidate).first()
        if not exists:
            return candidate
    return f"user_{secrets.token_hex(4)}"


def get_or_create_user_by_email(db: Session, email: str) -> User:
    normalized = normalize_email(email)
    user = db.query(User).filter(User.email == normalized).first()
    if user:
        return user

    display = normalized.split("@", 1)[0]
    user = User(
        username=_unique_username(db, normalized),
        email=normalized,
        display_name=display,
    )
    db.add(user)
    db.commit()
    db.refresh(user)
    return user
