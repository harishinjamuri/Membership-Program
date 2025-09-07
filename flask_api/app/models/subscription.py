from app.models.base import BaseModel, db

from datetime import datetime

from app.constants import SubscriptionStatus, TierType


class Subscription(BaseModel):
    __tablename__ = "subscription"

    user_id = db.Column(db.String(36), nullable=False)
    plan_id = db.Column(db.String(36), nullable=False)
    tier_id = db.Column(db.String(36), nullable=False)
    current_tier = db.Column(db.Integer, default=TierType.BRONZE.value)
    benefits = db.Column(db.JSON, nullable=False)
    start_date = db.Column(db.DateTime, nullable=False)
    end_date = db.Column(db.DateTime, nullable=False)
    auto_renew = db.Column(db.Boolean, default=True)
    subscription_number = db.Column(db.String(36), nullable=False)
    total_saved = db.Column(db.Float, default=0)
    total_spent = db.Column(db.Float, default=0)
    is_active = db.Column(db.Boolean, default=True)

    def __repr__(self):
        return f"<Subscription {self.subscription_number}>"

    @property
    def status(self):
        if self.is_expired:
            return SubscriptionStatus.EXPIRED.value
        else:
            return SubscriptionStatus.ACTIVE.value

    @property
    def days_remaining(self):
        """Returns the number of days until the subscription ends."""
        today = datetime.utcnow()
        if self.end_date < today:
            return 0
        return (self.end_date - today).days

    @property
    def is_expired(self):
        """Returns True if the subscription has expired."""
        return datetime.utcnow() > self.end_date

    @property
    def is_currently_active(self):
        """Returns True if the subscription is currently active and not expired."""
        return self.is_active and not self.is_expired

    @property
    def duration_in_days(self):
        """Returns the total duration of the subscription in days."""
        return (self.end_date - self.start_date).days

    @property
    def progress_percentage(self):
        """Returns how much of the subscription period has been used (0 to 100%)."""
        today = datetime.utcnow()
        total_days = self.duration_in_days
        used_days = (today - self.start_date).days
        if total_days <= 0:
            return 100 if self.is_expired else 0
        return min(100, max(0, int((used_days / total_days) * 100)))
