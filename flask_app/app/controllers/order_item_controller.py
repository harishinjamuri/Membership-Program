from flask import request
from flask_jwt_extended import jwt_required
from app.services.order_item_service import OrderItemService
from app.utils.response import success_response, error_response

class OrderItemController:

    @staticmethod
    @jwt_required()
    def get_by_order(order_id):
        items = OrderItemService.get_all_for_order(order_id)
        return success_response("Order items retrieved", [i.to_dict() for i in items])

    @staticmethod
    @jwt_required()
    def get_one(order_item_id):
        oi = OrderItemService.get_by_id(order_item_id)
        if not oi:
            return error_response("Order item not found", 404)
        return success_response("Order item found", oi.to_dict())

    @staticmethod
    @jwt_required()
    def create(order_id):
        data = request.get_json()
        oi = OrderItemService.create(order_id, data)
        return success_response("Order item created", oi.to_dict())

    @staticmethod
    @jwt_required()
    def update(order_item_id):
        data = request.get_json()
        updated = OrderItemService.update(order_item_id, data)
        if not updated:
            return error_response("Order item not found", 404)
        return success_response("Order item updated", updated.to_dict())

    @staticmethod
    @jwt_required()
    def delete(order_id):
        success, message = OrderItemService.delete(order_id)

        if not success:
            return error_response(message, 404)

        return success_response(message)
    

