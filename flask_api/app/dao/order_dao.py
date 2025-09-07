from app.models.order import Order
from app.models import db
from sqlalchemy import func


class OrderDAO:

    @staticmethod
    def get_by_filter(page=1, per_page=10, filters=None):
        query = Order.query
        filters = filters or {}

        if filters.get("status"):
            query = query.filter(Order.status == filters["status"])

        if filters.get("user_id"):
            query = query.filter(Order.user_id == filters["user_id"])

        pagination = query.paginate(page=page, per_page=per_page, error_out=False)
        return pagination.items, pagination.total

    @staticmethod
    def get_all_for_user(page=1, per_page=10, filters=None):
        return OrderDAO.get_by_filter(page=page, per_page=per_page, filters=filters)

    @staticmethod
    def get_by_id(order_id):
        return Order.query.filter_by(id=order_id).first()

    @staticmethod
    def get_all_orders(page=1, per_page=10, filters=None):
        return OrderDAO.get_by_filter(page=page, per_page=per_page, filters=filters)

    @staticmethod
    def get_total_orders(user_id):
        return Order.query.filter_by(user_id=user_id).count()

    @staticmethod
    def get_total_spend(user_id):
        return (
            Order.query.filter_by(user_id=user_id)
            .with_entities(func.sum(Order.total_amount))
            .scalar()
            or 0
        )

    @staticmethod
    def create(data):
        order = Order(**data)
        db.session.add(order)
        db.session.commit()
        return order

    @staticmethod
    def save(order):
        db.session.add(order)
        db.session.commit()
        return order

    @staticmethod
    def update(order_id, data):
        order = OrderDAO.get_by_id(order_id)
        if not order:
            return None
        for key, value in data.items():
            if hasattr(order, key):
                setattr(order, key, value)
        db.session.commit()
        return order

    @staticmethod
    def delete(order_id):
        order = OrderDAO.get_by_id(order_id)
        if not order:
            return False
        db.session.delete(order)
        db.session.commit()
        return True
