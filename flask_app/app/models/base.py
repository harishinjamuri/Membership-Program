from enum import Enum
import uuid
from datetime import datetime
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import declared_attr

from app.constants import UserType


db = SQLAlchemy()

class BaseModel(db.Model):
    """Abstract base with common fields."""
    __abstract__ = True


    id = db.Column(db.String(36), primary_key=True, default=lambda: str(uuid.uuid4()), nullable=False)
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    updated_at = db.Column(db.DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

    @declared_attr
    def __tablename__(cls):
        return cls.__name__.lower()

    def to_dict(self):
        result = {}
        for c in self.__table__.columns:
            if self.__tablename__ == 'users' and c.name == 'password_hash' :
                continue
            value = getattr(self, c.name)
            if isinstance(value, Enum):
                value = value.name
            result[c.name] = value
        return result
