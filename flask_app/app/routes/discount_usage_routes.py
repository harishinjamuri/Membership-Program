from flask import Blueprint
from app.controllers.discount_usage_controller import DiscountUsageController

discount_usage_bp = Blueprint('discount_usage', __name__, url_prefix='/discount-usages')

discount_usage_bp.get('/')(DiscountUsageController.get_all)
discount_usage_bp.get('/<usage_id>')(DiscountUsageController.get_one)
discount_usage_bp.get('/me')(DiscountUsageController.get_my_usages)
