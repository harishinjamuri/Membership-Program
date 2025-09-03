from pydantic import BaseModel
from app.constants import BenefitType

# benefit_type: DISCOUNT = "Discount"
class DiscountBenefitModel(BaseModel):
    percent: float
    discount_id: str

# benefit_type: FREE_SHIPPING = "Free_shipping"
class FreeShippingBenefitModel(BaseModel):
    enabled: bool
    min_order_value: float = 0.0

# benefit_type: EARLY_ACCESS = "Early_access"
class EarlyAccessBenefitModel(BaseModel):
    enabled: bool
    hours_before: int

class PrioritySupportBenefitModel(BaseModel):
    type: str


model_map = {
    BenefitType.DISCOUNT: DiscountBenefitModel,
    BenefitType.FREE_SHIPPING: FreeShippingBenefitModel,
    BenefitType.EARLY_ACCESS: EarlyAccessBenefitModel,
    BenefitType.PRIORITY_SUPPORT: PrioritySupportBenefitModel
}

def validate_benefit(benefit_type_str: str, benefit_value: dict):
    try:
        benefit_type = BenefitType[benefit_type_str]
    except KeyError:
        raise ValueError(f"Invalid benefit_type: {benefit_type_str}")

    model_cls = model_map.get(benefit_type)
    if not model_cls:
        raise ValueError(f"Invalid benefit_type: {benefit_type}")

    return model_cls(**benefit_value)