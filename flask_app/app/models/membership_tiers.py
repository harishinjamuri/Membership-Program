from sqlalchemy.orm import validates
from app.models.base import BaseModel, db
from app.constants import TierType

class MemberShipTiers(BaseModel):
    __tablename__ = 'membership_tiers'

    name = db.Column(db.String(50), nullable=False)
    description = db.Column(db.Text, nullable=False)
    is_active = db.Column(db.Boolean, default=True)
    tier = db.Column(db.Enum(TierType), nullable=False)
    tier_level = db.Column(db.Integer, default=1, nullable=False)
    min_monthly_orders = db.Column(db.Integer, nullable=False)
    min_monthly_spend = db.Column(db.Float, nullable=False)
    min_total_orders = db.Column(db.Integer, nullable=False)
    min_total_spend = db.Column(db.Float, nullable=False)

    @validates('tier')
    def _set_tier_level_from_tier(self, key, value):
        if not self.tier_level:
            self.tier_level = TierType[value].value
        return value

    def __repr__(self):
        return f"<MemberShipTiers {self.name}>"
