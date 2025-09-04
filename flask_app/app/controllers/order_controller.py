from flask import request
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.services.order_service import OrderService
from app.utils.response import success_response, error_response


class OrderController:

    @staticmethod
    @jwt_required()
    def get_my_orders():
        user_id = get_jwt_identity()
        page = request.args.get("page", default=1, type=int)
        per_page = request.args.get("per_page", default=10, type=int)

        filters = {"user_id": user_id}

        filters = {k: v for k, v in filters.items() if v is not None}

        orders, total = OrderService.get_all_for_user(page, per_page, filters)
        return success_response(
            "Orders retrieved",
            {
                "items": [o.to_dict() for o in orders],
                "total": total,
                "page": page,
                "per_page": per_page,
            },
        )

    @staticmethod
    @jwt_required()
    def get_all_orders():
        page = request.args.get("page", default=1, type=int)
        per_page = request.args.get("per_page", default=10, type=int)

        filters = {
            "status": request.args.get("status"),
            "user_id": request.args.get("request_id"),
        }

        filters = {k: v for k, v in filters.items() if v is not None}

        orders, total = OrderService.get_all_orders(page, per_page, filters)
        return success_response(
            "Orders retrieved",
            {
                "items": [o.to_dict() for o in orders],
                "total": total,
                "page": page,
                "per_page": per_page,
            },
        )

    @staticmethod
    @jwt_required()
    def get_one(order_id):
        order = OrderService.get_by_id(order_id)
        if not order:
            return error_response("Order not found", 404)
        return success_response("Order found", order.to_dict())

    @staticmethod
    @jwt_required()
    def create():
        data = request.get_json()
        data["user_id"] = get_jwt_identity()
        order = OrderService.create(data)
        return success_response("Order created", order.to_dict())

    @staticmethod
    @jwt_required()
    def update(order_id):
        data = request.get_json()
        updated = OrderService.update(order_id, data)
        if not updated:
            return error_response("Order not found", 404)
        return success_response("Order updated", updated.to_dict())

    @staticmethod
    @jwt_required()
    def confirm_order(order_id):
        user_id = get_jwt_identity()
        updated = OrderService.confirm(user_id, order_id)
        if not updated:
            return error_response("Order not found", 404)
        return success_response("Order updated", updated.to_dict())
