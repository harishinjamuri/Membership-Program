from app.models.base import BaseModel, db
from app.constants import EntityType


class DiscountUsage(BaseModel):
    __tablename__ = "discount_usage"

    discount_id = db.Column(db.String(36), nullable=False)
    user_id = db.Column(db.String(36), nullable=False)
    applied_on = db.Column(db.String(36), nullable=False)
    entity = db.Column(db.Enum(EntityType), nullable=False)
    discount_amount = db.Column(db.Float, nullable=False)
    used_at = db.Column(db.DateTime, nullable=False)

    def __repr__(self):
        return f"<DiscountUsage {self.discount_id}>"
