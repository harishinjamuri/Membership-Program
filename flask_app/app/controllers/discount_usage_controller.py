from flask import request
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.services.discount_usage_service import DiscountUsageService
from app.utils.response import success_response, error_response
from app.utils.decorators import admin_required


class DiscountUsageController:

    @staticmethod
    @admin_required()
    def get_all():
        page = request.args.get("page", default=1, type=int)
        per_page = request.args.get("per_page", default=10, type=int)

        filters = {
            "discount_id": request.args.get("discount_id"),
            "user_id": request.args.get("user_id"),
        }

        filters = {k: v for k, v in filters.items() if v is not None}

        usages, total = DiscountUsageService.get_all(page, per_page, filters=filters)
        return success_response(
            "Discount usages retrieved",
            {
                "items": [u.to_dict() for u in usages],
                "total": total,
                "page": page,
                "per_page": per_page,
            },
        )

    @staticmethod
    @jwt_required()
    def get_one(usage_id):
        usage = DiscountUsageService.get_by_id(usage_id)
        if not usage:
            return error_response("Discount usage not found", 404)
        return success_response("Discount usage found", usage.to_dict())

    @staticmethod
    @jwt_required()
    def get_my_usages():
        current_user_id = get_jwt_identity()
        usages = DiscountUsageService.get_by_user(current_user_id)
        return success_response(
            "Your discount usages retrieved", [u.to_dict() for u in usages]
        )
