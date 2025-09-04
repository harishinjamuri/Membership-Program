from app.dao.user_dao import UserDAO


class UserService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return UserDAO.get_all(page, per_page, filters)

    @staticmethod
    def get_by_id(user_id):
        return UserDAO.get_by_id(user_id)

    @staticmethod
    def update(user_id, data):
        return UserDAO.update(user_id, data)

    @staticmethod
    def soft_delete(user_id):
        return UserDAO.soft_delete(user_id)

    @staticmethod
    def is_admin(user_id):
        user = UserDAO.get_by_id(user_id)
        return user.is_admin
