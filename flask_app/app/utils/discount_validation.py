from app.services.discount_service import DiscountService
from app.schemas.discount import AppliesToModel
from app.constants import DiscountType, DiscountApplicableType

def check_product_discount_eligibility(item):
    discount_id = item.get('discount_id')
    if not discount_id:
        return False, 0

    discount = DiscountService.get_by_id(discount_id)
    if not discount:
        return False, 0

    if discount.applies_to_type == DiscountApplicableType.Product.value  and item['id'] in discount.applicable_ids and discount.is_currently_active :
        applied,value = apply_discount(discount.discount_type, item['price'], discount.value)
        return applied, value
    return False, 0


def apply_discount(discount_type, price, discount_value ):
    if discount_type == DiscountType.FIXED_AMOUNT:
        return True, discount_value
    elif discount_type == DiscountType.PERCENTAGE:
        discount_value = round(price * (discount_value / 100), 2)
        return True, discount_value
