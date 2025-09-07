from flask import request
from app.services.membership_tier_service import MembershipTierService
from app.utils.response import success_response, error_response
from app.utils.decorators import admin_required


class MembershipTierController:

    @staticmethod
    def get_all():
        page = request.args.get("page", default=1, type=int)
        per_page = request.args.get("per_page", default=10, type=int)

        filters = {
            "is_active": request.args.get(
                "active", type=lambda v: v.lower() == "true" if v else None
            ),
            "tier_level": request.args.get("tier_level"),
        }

        filters = {k: v for k, v in filters.items() if v is not None}

        tiers, total = MembershipTierService.get_all(page, per_page, filters=filters)
        return success_response(
            "Membership Tiers retrieved",
            {
                "items": [t.to_dict() for t in tiers],
                "total": total,
                "page": page,
                "per_page": per_page,
            },
        )

    @staticmethod
    def get_one(tier_id):
        tier = MembershipTierService.get_by_id(tier_id)
        if not tier:
            return error_response("Tier not found", 404)
        return success_response("Tier fetched", tier.to_dict())

    @staticmethod
    @admin_required()
    def create():
        data = request.get_json()
        try:
            tier = MembershipTierService.create(data)
            return success_response("Tier created", tier.to_dict(), 201)
        except Exception as e:
            return error_response(str(e), 400)

    @staticmethod
    @admin_required()
    def update(tier_id):
        data = request.get_json()
        updated = MembershipTierService.update(tier_id, data)
        if not updated:
            return error_response("Tier not found", 404)
        return success_response("Tier updated", updated.to_dict())

    @staticmethod
    @admin_required()
    def delete(tier_id):
        success = MembershipTierService.soft_delete(tier_id)
        if not success:
            return error_response("Tier not found", 404)
        return success_response("Deleted successfully")
