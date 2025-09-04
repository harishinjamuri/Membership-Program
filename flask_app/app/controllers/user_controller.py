from flask import request
from flask_jwt_extended import jwt_required
from app.services.user_service import UserService
from app.utils.response import success_response, error_response
from app.utils.decorators import admin_required


class UserController:
    @staticmethod
    @admin_required()
    def get_all():
        page = request.args.get("page", default=1, type=int)
        per_page = request.args.get("per_page", default=10, type=int)

        filters = {"status": request.args.get("status")}

        filters = {k: v for k, v in filters.items() if v is not None}

        users, total = UserService.get_all(page, per_page, filters)
        return success_response(
            "Users retrieved",
            {
                "items": [u.to_dict() for u in users],
                "total": total,
                "page": page,
                "per_page": per_page,
            },
        )

    @staticmethod
    @jwt_required()
    def get_one(user_id):
        user = UserService.get_by_id(user_id)
        if not user:
            return error_response("User not found", 404)
        return success_response("User found", user.to_dict())

    @staticmethod
    @jwt_required()
    def update(user_id):
        data = request.get_json()
        updated_user = UserService.update(user_id, data)
        if not updated_user:
            return error_response("User not found", 404)
        return success_response("User updated", updated_user.to_dict())

    @staticmethod
    @admin_required()
    def delete(user_id):
        deleted = UserService.soft_delete(user_id)
        if not deleted:
            return error_response("User not found", 404)
        return success_response("User deleted successfully")
