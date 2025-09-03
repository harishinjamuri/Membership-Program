from app.models.base import BaseModel, db

class OrderItem(BaseModel):
    __tablename__ = 'order_item'

    order_id = db.Column(db.String(36), nullable=False)
    product_id = db.Column(db.String(36), nullable=False)
    product_name = db.Column(db.String(100), nullable=False)
    quantity = db.Column(db.Integer, nullable=False)
    unit_price = db.Column(db.Float, nullable=False)
    total_price = db.Column(db.Float, nullable=False)
    discount_applied = db.Column(db.Boolean, default=False, nullable=False)
    discount_value = db.Column(db.Float, default=0.0, nullable=False)
    discount_id = db.Column(db.String(36), nullable=False)
    total_discount_value = db.Column(db.Float, default=0.0, nullable=False)

    def __repr__(self):
        return f"<OrderItem {self.id} - Order {self.order_id}>"

    @property
    def original_total(self):
        return round(self.unit_price * self.quantity, 2)

    @property
    def effective_total(self):
        discount = self.discount_value if self.discount_applied else 0.0
        return round(self.original_total - discount, 2)
