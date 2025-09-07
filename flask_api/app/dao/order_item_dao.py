from app.models.order_item import OrderItem
from app.models import db


class OrderItemDAO:

    @staticmethod
    def get_all_for_order(order_id):
        return OrderItem.query.filter_by(order_id=order_id).all()

    @staticmethod
    def get_by_id(order_item_id):
        return OrderItem.query.filter_by(id=order_item_id).first()

    @staticmethod
    def get_by_ids(order_item_ids):
        return OrderItem.query.filter(OrderItem.id.in_(order_item_ids)).all()

    @staticmethod
    def create(order_id, data):
        data["order_id"] = order_id
        oi = OrderItem(**data)
        db.session.add(oi)
        db.session.commit()
        return oi

    @staticmethod
    def update(order_item_id, data):
        oi = OrderItemDAO.get_by_id(order_item_id)
        if not oi:
            return None
        for key, value in data.items():
            if hasattr(oi, key):
                setattr(oi, key, value)
        db.session.commit()
        return oi

    @staticmethod
    def delete(order_item_id):
        oi = OrderItemDAO.get_by_id(order_item_id)
        if not oi:
            return False
        db.session.delete(oi)
        db.session.commit()
        return True
