from flask import current_app
from app.dao.tier_benefit_dao import TierBenefitDAO
from app.schemas.tier_benefits import validate_benefit
from pydantic import ValidationError


class TierBenefitService:

    @staticmethod
    def get_all(is_active=True):
        return TierBenefitDAO.get_all(is_active)

    @staticmethod
    def get_by_id(benefit_id):
        return TierBenefitDAO.get_by_id(benefit_id)

    @staticmethod
    def get_by_tier_id(tier_id):
        return TierBenefitDAO.get_by_tier_id(tier_id)

    @staticmethod
    def create(data):
        try:
            validated_benefit_value = validate_benefit(
                data["benefit_type"], data["benefit_value"]
            )
            data["benefit_value"] = validated_benefit_value.dict()
        except ValidationError as e:
            current_app.logger.exception("Invalid benefit_value:", e)

        return TierBenefitDAO.create(data)

    @staticmethod
    def update(benefit_id, data):
        existing = TierBenefitDAO.get_by_id(benefit_id)
        if not existing:
            return None

        # Merge existing data and incoming data
        merged_data = existing.to_dict() | data

        try:
            validated_benefit_value = validate_benefit(
                merged_data["benefit_type"], merged_data["benefit_value"]
            )
            merged_data["benefit_value"] = validated_benefit_value.dict()
        except ValidationError as e:
            current_app.logger.exception("Invalid benefit_value:", e)
            raise ValueError(f"Validation failed: {e}")

        return TierBenefitDAO.update(existing, merged_data)

    @staticmethod
    def soft_delete(benefit_id):
        return TierBenefitDAO.soft_delete(benefit_id)
