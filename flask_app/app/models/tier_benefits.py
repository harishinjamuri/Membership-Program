from app.models.base import BaseModel, db
from app.constants import BenefitType

class TierBenefits(BaseModel):
    __tablename__ = 'tier_benefits'

    description = db.Column(db.Text, nullable=False)
    tier_id = db.Column(db.String(36), nullable=False)
    is_active = db.Column(db.Boolean, default=True)
    benefit_type  = db.Column(db.Enum(BenefitType), nullable=False)
    benefit_value = db.Column(db.JSON, nullable=False)
    sort_order = db.Column(db.Integer)
    
    def __repr__(self):
        return f"<TierBenefits {self.description}>"