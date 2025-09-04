from app import db
from app.models.user import User
from datetime import datetime

from app.utils.security import hash_password


class AuthDAO:

    @staticmethod
    def get_by_email(email):
        return User.query.filter_by(email=email).first()

    @staticmethod
    def get_by_id(user_id):
        return User.query.get(user_id)

    @staticmethod
    def create_user(name, email, password_hash, phone_number=None, date_of_birth=None):
        user = User(
            name=name,
            email=email,
            password_hash=password_hash,
            phone_number=phone_number,
            date_of_birth=date_of_birth,
            created_at=datetime.utcnow(),
            updated_at=datetime.utcnow(),
        )
        db.session.add(user)
        db.session.commit()
        return user

    @staticmethod
    def change_password(user_id, new_password):
        user = User.query.filter_by(id=user_id, status=True).first()
        if not user:
            return None

        user.password_hash = hash_password(new_password)
        db.session.commit()
        return user
