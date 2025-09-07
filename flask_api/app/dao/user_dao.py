from app.models.user import User
from app.models import db
from app.constants import UserStatus


class UserDAO:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        query = User.query

        filters = filters or {}
        if filters.get("status"):
            query.filter_by(User.status == filters["status"])

        if filters.get("user_type"):
            query.filter_by(User.status == filters["user_type"])

        pagination = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_by_id(user_id):
        return User.query.filter_by(id=user_id, status=UserStatus.ACTIVE).first()

    @staticmethod
    def update(user_id, data):
        user = UserDAO.get_by_id(user_id)
        if not user:
            return None

        for key, value in data.items():
            if hasattr(user, key):
                setattr(user, key, value)

        db.session.commit()
        return user

    @staticmethod
    def soft_delete(user_id):
        user = UserDAO.get_by_id(user_id)
        if not user:
            return False
        user.status = False
        db.session.commit()
        return True
