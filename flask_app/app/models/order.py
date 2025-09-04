from app.models.base import BaseModel, db
from app.constants import OrderStatus


class Order(BaseModel):
    __tablename__ = "order"

    user_id = db.Column(db.String(36), nullable=False)
    status = db.Column(
        db.Enum(OrderStatus), default=OrderStatus.PENDING, nullable=False
    )
    total_amount = db.Column(db.Float, nullable=False, default=0.0)
    total_savings = db.Column(db.Float, nullable=False, default=0.0)
    subtotal = db.Column(db.Float, nullable=False, default=0.0)

    delivery_address = db.Column(db.Text, nullable=False)
    delivery_fee = db.Column(db.Float, nullable=False, default=0.0)
    delivery_fee_applied = db.Column(db.Boolean, nullable=False, default=True)

    items = db.Column(db.JSON, nullable=False, default=[])
    discounts = db.Column(db.JSON, nullable=False, default=[])

    @property
    def order_item_count(self):
        return len(self.items) if self.items else 0

    def __repr__(self):
        return f"<Order {self.id}>"
