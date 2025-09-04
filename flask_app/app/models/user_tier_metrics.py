from datetime import datetime
from app.models.base import BaseModel, db


class UserTierMetrics(BaseModel):
    __tablename__ = "user_tier_metrics"

    user_id = db.Column(db.String(36), nullable=False)
    month_year = db.Column(db.String(7), nullable=False)  # Format: YYYY-MM
    order_count = db.Column(db.Integer, nullable=False)
    total_spent = db.Column(db.Float, nullable=False)

    @property
    def month_name(self):
        """Returns month name like 'September 2025' from month_year."""
        try:
            date_obj = datetime.strptime(self.month_year, "%Y-%m")
            return date_obj.strftime("%B %Y")
        except ValueError:
            return self.month_year

    @property
    def average_order_value(self):
        """Returns average value per order."""
        if self.order_count == 0:
            return 0.0
        return round(self.total_spent / self.order_count, 2)

    def __repr__(self):
        return f"<UserTierMetrics user_id={self.user_id} month_year={self.month_year}>"
