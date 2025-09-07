from app.models.subscription import Subscription
from app.models.base import db


class SubscriptionDAO:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        query = Subscription.query
        filters = filters or {}

        if filters.get("user_id"):
            query = query.filter(Subscription.user_id == filters["user_id"])

        if filters.get("is_active") is not None:
            query = query.filter(Subscription.is_active == filters["is_active"])

        query = query.order_by(Subscription.start_date.desc())
        pagination = query.paginate(page=page, per_page=per_page, error_out=False)

        return pagination.items, pagination.total

    @staticmethod
    def get_by_id(subscription_id):
        return Subscription.query.filter_by(id=subscription_id, is_active=True).first()

    @staticmethod
    def create(data):
        subscription = Subscription(**data)
        db.session.add(subscription)
        db.session.commit()
        return subscription

    @staticmethod
    def get_active_by_user(user_id):
        return (
            Subscription.query.filter_by(user_id=user_id, is_active=True)
            .order_by(Subscription.end_date.desc())
            .first()
        )

    @staticmethod
    def save(subscription):
        db.session.add(subscription)
        db.session.commit()
        return subscription

    @staticmethod
    def update(subscription_id, data):
        subscription = SubscriptionDAO.get_by_id(subscription_id)
        if not subscription:
            return None
        for key, value in data.items():
            setattr(subscription, key, value)
        db.session.commit()
        return subscription

    @staticmethod
    def soft_delete(subscription_id):
        subscription = SubscriptionDAO.get_by_id(subscription_id)
        if not subscription:
            return False
        subscription.is_active = False
        db.session.commit()
        return True
