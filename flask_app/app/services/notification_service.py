from app.dao.notification_dao import NotificationDAO

class NotificationService:

    @staticmethod
    def get_all_for_user(user_id, page, per_page,filters=None):
        return NotificationDAO.get_all_for_user(user_id, page, per_page, filters)

    @staticmethod
    def create(data):
        return NotificationDAO.create(data)

    @staticmethod
    def mark_as_read(notification_id):
        return NotificationDAO.mark_as_read(notification_id)

    @staticmethod
    def get_by_id(notification_id):
        return NotificationDAO.get_by_id(notification_id)

    @staticmethod
    def mark_all_as_read(user_id):
        NotificationDAO.mark_all_as_read(user_id)