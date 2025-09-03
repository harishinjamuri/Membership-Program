from app.models.base import BaseModel, db
from datetime import datetime

from app.constants import DiscountType

class Discount(BaseModel):
    __tablename__ = 'discount'

    code  = db.Column(db.String(36), nullable=False)
    name  = db.Column(db.String(100), nullable=False)
    description  = db.Column(db.Text, nullable=False)
    discount_type  = db.Column(db.Enum(DiscountType), nullable=False)
    value = db.Column(db.Float, default=0)
    applies_to = db.Column(db.JSON, nullable=False)
    start_date = db.Column(db.DateTime, nullable=False)
    end_date = db.Column(db.DateTime, nullable=False)
    is_active = db.Column(db.Boolean, default=True)
    usage_count = db.Column(db.Integer, default=0)
    min_order_value = db.Column(db.Float, default=0)
    max_discount_amount = db.Column(db.Float, default=0)
    min_tier = db.Column(db.Integer, nullable=False)

    @property
    def is_expired(self):
        """Returns True if discount is past its end date."""
        return datetime.utcnow() > self.end_date

    @property
    def is_upcoming(self):
        """Returns True if discount is scheduled to start in future."""
        return datetime.utcnow() < self.start_date

    @property
    def is_currently_active(self):
        """Checks if the discount is active and within date range."""
        now = datetime.utcnow()
        return self.is_active and self.start_date <= now <= self.end_date

    @property
    def applies_to_type(self):
        """Get discount applicability target (product/category/all)."""
        return self.applies_to.get("type","")

    @property
    def applicable_ids(self):
        """Return the product/category IDs the discount applies to."""
        return self.applies_to.get("ids", [])

    @property
    def is_first_time_user_only(self):
        """True if discount is for first-time users only."""
        return self.applies_to.get("first_time_user_only", False)

    @property
    def auto_apply(self):
        """True if this discount is auto-applied at checkout."""
        return self.applies_to.get("auto_apply", False)

    def __repr__(self):
        return f"<Discount {self.code}>"
