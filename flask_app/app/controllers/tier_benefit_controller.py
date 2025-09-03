from flask import request
from app.services.tier_benefit_service import TierBenefitService
from app.utils.response import success_response, error_response
from app.utils.decorators import admin_required

class TierBenefitController:

    @staticmethod
    def get_all():
        is_active = request.args.get('active', default=True, type=bool)
        benefits = TierBenefitService.get_all(is_active)
        return success_response("Tier benefits fetched", [b.to_dict() for b in benefits])

    @staticmethod
    def get_one(benefit_id):
        benefit = TierBenefitService.get_by_id(benefit_id)
        if not benefit:
            return error_response("Benefit not found", 404)
        return success_response("Benefit fetched", benefit.to_dict())

    @staticmethod
    def get_by_tier(tier_id):
        benefits = TierBenefitService.get_by_tier_id(tier_id)
        if not benefits:
            return error_response("No benefits found for the given tier", 404)
        return success_response("Benefits by tier fetched", [b.to_dict() for b in benefits])

    @staticmethod
    @admin_required()
    def create():
        data = request.get_json()
        try:
            benefit = TierBenefitService.create(data)
            return success_response("Benefit created", benefit.to_dict(), 201)
        except Exception as e:
            return error_response(str(e), 400)

    @staticmethod
    @admin_required()
    def update(benefit_id):
        data = request.get_json()
        benefit = TierBenefitService.update(benefit_id, data)
        if not benefit:
            return error_response("Benefit not found", 404)
        return success_response("Benefit updated", benefit.to_dict())

    @staticmethod
    @admin_required()
    def delete(benefit_id):
        success = TierBenefitService.soft_delete(benefit_id)
        if not success:
            return error_response("Benefit not found", 404)
        return success_response("Benefit deleted")
