from flask import request
from flask_jwt_extended import jwt_required
from app.services.category_service import CategoryService
from app.utils.response import success_response, error_response
from app.utils.decorators import admin_required

class CategoryController:

    @staticmethod
    @jwt_required()
    def get_all():
        page = request.args.get('page', default=1, type=int)
        per_page = request.args.get('per_page', default=10, type=int)
        filters = {
            "is_active": request.args.get("active", type=lambda v: v.lower() == 'true' if v else None),
        }

        filters = {k: v for k, v in filters.items() if v is not None}

        tiers, total = CategoryService.get_all(page, per_page)
        return success_response("Categories retrieved", {
            "items": [t.to_dict() for t in tiers],
            "total": total,
            "page": page,
            "per_page": per_page,
        })
    
    @staticmethod
    @jwt_required()
    def get_one(category_id):
        category = CategoryService.get_by_id(category_id)
        if not category:
            return error_response("Category not found", 404)
        return success_response("Category found", category.to_dict())

    @staticmethod
    @admin_required()
    def create():
        data = request.get_json()
        category = CategoryService.create(data)
        return success_response("Category created", category.to_dict())

    @staticmethod
    @admin_required()
    def update(category_id):
        data = request.get_json()
        updated_category = CategoryService.update(category_id, data)
        if not updated_category:
            return error_response("Category not found", 404)
        return success_response("Category updated", updated_category.to_dict())

    @staticmethod
    @admin_required()
    def delete(category_id):
        deleted = CategoryService.soft_delete(category_id)
        if not deleted:
            return error_response("Category not found", 404)
        return success_response("Category deleted successfully")
