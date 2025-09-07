from flask import Blueprint
from app.controllers.order_item_controller import OrderItemController

order_item_bp = Blueprint("order_item", __name__, url_prefix="/order-items")

order_item_bp.get("/order/<order_id>")(OrderItemController.get_by_order)
order_item_bp.get("/<order_item_id>")(OrderItemController.get_one)
order_item_bp.post("/<order_id>")(OrderItemController.create)
order_item_bp.put("/<order_item_id>")(OrderItemController.update)
order_item_bp.delete("/<order_id>/<order_item_id>")(OrderItemController.delete)
