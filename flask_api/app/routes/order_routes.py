from flask import Blueprint
from app.controllers.order_controller import OrderController

order_bp = Blueprint("order", __name__, url_prefix="/orders")

order_bp.get("/me")(OrderController.get_my_orders)
order_bp.get("/")(OrderController.get_all_orders)
order_bp.get("/<order_id>")(OrderController.get_one)
order_bp.post("/")(OrderController.create)
order_bp.put("/<order_id>")(OrderController.update)
order_bp.put("/confirm/<order_id>")(OrderController.confirm_order)
