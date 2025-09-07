from datetime import datetime
from app.dao.discount_dao import DiscountDAO
from app.constants import DiscountType, TierType
from app.schemas.discount import AppliesToModel
from pydantic import ValidationError


class DiscountService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return DiscountDAO.get_all(page=page, per_page=per_page, filters=filters or {})

    @staticmethod
    def get_by_id(discount_id):
        return DiscountDAO.get_by_id(discount_id)

    @staticmethod
    def get_by_code(code):
        return DiscountDAO.get_by_code(code)

    @staticmethod
    def create(data):
        if data.get("code"):
            discount = DiscountDAO.get_by_code(data["code"])
            if discount:
                return f"Discount Already Exist with Code {data['code']}", False

        try:
            applies_to_validated = AppliesToModel(**data["applies_to"])

            data["applies_to"] = applies_to_validated.model_dump()
        except ValidationError as e:
            raise ValueError(f"Invalid applies_to format: {e}")

        try:
            data["discount_type"] = DiscountType[data["discount_type"].upper()]
        except KeyError:
            raise ValueError(f"Invalid discount_type: {data.get('discount_type')}")

        start_date = data.get("start_date")
        end_date = data.get("end_date")
        if start_date:
            start_date = datetime.strptime(start_date, "%Y/%m/%d")
        else:
            start_date = datetime.utcnow()
        if end_date:
            end_date = datetime.strptime(end_date, "%Y/%m/%d")
        else:
            end_date = datetime(9999, 12, 31)  # lasts forever

        data["start_date"] = start_date
        data["end_date"] = end_date
        data["min_tier"] = TierType[data["min_tier"]].value
        return DiscountDAO.create(data), True

    @staticmethod
    def update(discount_id, data):
        start_date = data.get("start_date")
        end_date = data.get("end_date")
        if start_date:
            start_date = datetime.strptime(start_date, "%Y/%m/%d")
            data["start_date"] = start_date
        if end_date:
            end_date = datetime.strptime(end_date, "%Y/%m/%d")
            data["end_date"] = end_date
        if data.get("min_tier"):
            data["min_tier"] = TierType[data["min_tier"]].value
        return DiscountDAO.update(discount_id, data)

    @staticmethod
    def increment_usage_count(discount_id):
        discount = DiscountDAO.get_by_id(discount_id)
        if not discount:
            return False
        usage_count = discount.usage_count + 1
        return DiscountDAO.update(discount_id, {"usage_count": usage_count})

    @staticmethod
    def validate_discount(data):
        code = data.get("code")
        order_total = data.get("order_total")
        user_tier = data.get("user_tier")
        if not order_total or not user_tier:
            return False, "order_total and user_tier are required."

        discount = DiscountDAO.get_by_code(code)
        if not discount:
            return False, "Discount Not Valid"
        if (
            discount.min_tier <= TierType[user_tier].value
            and order_total >= discount.min_order_value
        ):
            return True, "Discount Valid"
        return False, "Discount Not Valid"

    @staticmethod
    def soft_delete(discount_id):
        return DiscountDAO.soft_delete(discount_id)
