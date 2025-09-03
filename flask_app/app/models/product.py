from app.models.base import BaseModel, db

class Product(BaseModel):
    __tablename__ = 'product'

    name = db.Column(db.String(100), unique=True, nullable=False)
    description = db.Column(db.Text, nullable=False)
    category_id  = db.Column(db.String(36), nullable=False)
    stock_quantity = db.Column(db.Integer, nullable=False, default=0)
    discount_id  = db.Column(db.String(36), nullable=True)
    price = db.Column(db.Float, nullable=False, default=0)
    is_membership_exclusive = db.Column(db.Boolean, default=False)
    min_tier_required = db.Column(db.Integer, nullable=False, default=1)
    
    @property
    def is_available(self):
        return self.stock_quantity > 0
    
    def __repr__(self):
        return f"<Product {self.name}>"
