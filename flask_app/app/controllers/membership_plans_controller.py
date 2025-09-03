from flask import request
from app.services.membership_plans_service import MembershipPlansService
from app.utils.response import success_response, error_response
from app.utils.decorators import admin_required


class MembershipPlansController:

    @staticmethod
    def get_all():
        page = request.args.get('page', default=1, type=int)
        per_page = request.args.get('per_page', default=10, type=int)

        filters = {
            "is_active": request.args.get("active", type=lambda v: v.lower() == 'true' if v else None),
        }

        filters = {k: v for k, v in filters.items() if v is not None}

        plans, total = MembershipPlansService.get_all(page, per_page, filters)
        return success_response("Membership plans retrieved", {
            "items": [p.to_dict() for p in plans],
            "total": total,
            "page": page,
            "per_page": per_page,
        })
    
    @staticmethod
    def get_one(plan_id):
        plan = MembershipPlansService.get_by_id(plan_id)
        if not plan:
            return error_response("Plan not found", 404)
        return success_response("Plan fetched", plan.to_dict())

    @staticmethod
    @admin_required()
    def create():
        data = request.get_json()
        try:
            plan = MembershipPlansService.create(data)
            return success_response("Plan created", plan.to_dict(), 201)
        except Exception as e:
            return error_response(str(e), 400)

    @staticmethod
    @admin_required()
    def update(plan_id):
        data = request.get_json()

        updated = MembershipPlansService.update(plan_id, data)
        if not updated:
            return error_response("Plan not found", 404)

        return success_response("Plan updated", updated.to_dict())

    @staticmethod
    @admin_required()
    def delete(plan_id):
        success = MembershipPlansService.soft_delete(plan_id)
        if not success:
            return error_response("Plan not found", 404)

        return success_response("Deleted successfully")
