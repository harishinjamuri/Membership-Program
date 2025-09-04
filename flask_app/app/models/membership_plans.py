from sqlalchemy.orm import validates
from app.models.base import BaseModel, db
from app.constants import PlanType


class MemberShipPlans(BaseModel):
    __tablename__ = "membership_plans"

    name = db.Column(db.String(50), nullable=False)
    description = db.Column(db.Text, nullable=False)
    price = db.Column(db.Float, nullable=False)
    is_active = db.Column(db.Boolean, default=True)
    duration_days = db.Column(db.Integer, nullable=False)
    plan_type = db.Column(db.Enum(PlanType), nullable=False)

    @validates("plan_type")
    def _set_duration_days_from_plan_type(self, key, value):
        if not self.duration_days:
            self.duration_days = PlanType[value].value
        return value

    def __repr__(self):
        return f"<MemberShipPlans {self.name}>"
