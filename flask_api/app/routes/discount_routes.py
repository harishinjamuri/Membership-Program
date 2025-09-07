from flask import Blueprint
from app.controllers.discount_controller import DiscountController

discount_bp = Blueprint("discount", __name__, url_prefix="/discounts")

discount_bp.get("")(DiscountController.get_all)
discount_bp.get("/code/<string:code>")(DiscountController.get_by_code)
discount_bp.get("/<string:discount_id>")(DiscountController.get_one)
discount_bp.post("")(DiscountController.create)
discount_bp.post("/validate")(DiscountController.validate_discount)
discount_bp.put("/<string:discount_id>")(DiscountController.update)
discount_bp.delete("/<string:discount_id>")(DiscountController.delete)
