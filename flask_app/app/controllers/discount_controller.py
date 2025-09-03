from flask import request
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.services.discount_service import DiscountService
from app.utils.response import success_response, error_response
from app.utils.decorators import admin_required

class DiscountController:

    @staticmethod
    @jwt_required()
    def get_all():
        page = request.args.get('page', default=1, type=int)
        per_page = request.args.get('per_page', default=10, type=int)
        
        filters = {
            "discount_type":request.args.get('discount_type'),
            "active":request.args.get("active", type=lambda v: v.lower() == 'true' if v else None),
        }

        filters = {k: v for k, v in filters.items() if v is not None}

        discounts, total = DiscountService.get_all(page, per_page, filters = filters)
        return success_response("Discounts list", {
            "items": [d.to_dict() for d in discounts],
            "total": total,
            "page": page,
            "per_page": per_page,
        })

    @staticmethod
    @jwt_required()
    def get_one(discount_id):
        discount = DiscountService.get_by_id(discount_id)
        if not discount:
            return error_response("Discount not found", 404)
        return success_response("Discounts Detail",discount.to_dict())
    
    @staticmethod
    @jwt_required()
    def get_by_code(code):
        discount = DiscountService.get_by_code(code)
        if not discount:
            return error_response("Discount not found", 404)
        return success_response("Discounts Detail",discount.to_dict())

    @staticmethod
    @admin_required()
    def create():
        data = request.get_json()
        try:
            discount, success = DiscountService.create(data)
            if success:
                return success_response("Discount Created",discount.to_dict(), 201)
            else:
                return error_response(discount, 400)
            
        except Exception as e:
            return error_response(str(e), 400)

    @staticmethod
    @jwt_required()
    def validate_discount():
        data = request.get_json()
        valid, msg = DiscountService.validate_discount(data)
        return success_response(msg, {"valid": valid})

    @staticmethod
    @admin_required()
    def update(discount_id):
        data = request.get_json()
        try:
            discount = DiscountService.update(discount_id, data)
            return success_response("Discount Updated",discount.to_dict())
        except Exception as e:
            return error_response(str(e), 400)

    @staticmethod
    @admin_required()
    def delete(discount_id):
        try:
            DiscountService.soft_delete(discount_id)
            return success_response({"message": "Deleted successfully"})
        except Exception as e:
            return error_response(str(e), 400)
