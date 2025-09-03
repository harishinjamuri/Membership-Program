from app.models.notifications import Notifications
from app.models import db
from sqlalchemy import and_
from datetime import datetime

class NotificationDAO:

    @staticmethod
    def get_all_for_user(user_id, page, per_page, filters=None):
        query = Notifications.query.filter_by(user_id=user_id)

        if filters:
            if filters.get("is_read") is not None:
                query = query.filter(Notifications.is_read == filters["is_read"])

            if filters.get("notification_type"):
                query = query.filter(Notifications.notification_type == filters["notification_type"])

            if filters.get("start_date"):
                query = query.filter(Notifications.created_at >= filters["start_date"])

            if filters.get("end_date"):
                query = query.filter(Notifications.created_at <= filters["end_date"])

        pagination = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_by_id(notification_id):
        return Notifications.query.filter_by(id=notification_id).first()

    @staticmethod
    def create(data):
        notification = Notifications(**data)
        db.session.add(notification)
        db.session.commit()
        return notification

    @staticmethod
    def mark_as_read(notification_id):
        notification = NotificationDAO.get_by_id(notification_id)
        if not notification:
            return None
        notification.is_read = True
        db.session.commit()
        return notification

    @staticmethod
    def mark_all_as_read(user_id):
        Notifications.query.filter_by(user_id=user_id, is_read=False).update({Notifications.is_read: True})
        db.session.commit()