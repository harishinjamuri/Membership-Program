from flask import Blueprint
from app.controllers.subscription_controller import SubscriptionController

subscription_bp = Blueprint("subscription_bp", __name__, url_prefix="/subscriptions")

subscription_bp.get("")(SubscriptionController.get_all)
subscription_bp.get("/<string:subscription_id>")(SubscriptionController.get_one)
subscription_bp.post("")(SubscriptionController.create)
subscription_bp.put("/<string:subscription_id>")(SubscriptionController.update)
subscription_bp.delete("/<string:subscription_id>")(SubscriptionController.delete)
subscription_bp.get("/active/<user_id>")(SubscriptionController.get_active_subscription)
subscription_bp.get("/auto_renew/<user_id>")(SubscriptionController.check_auto_renew)
