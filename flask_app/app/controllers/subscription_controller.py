from flask import request
from flask_jwt_extended import get_jwt_identity, jwt_required
from app.services.subscription_service import SubscriptionService
from app.utils.response import success_response, error_response

class SubscriptionController:

    @staticmethod
    @jwt_required()
    def get_all():
        page = request.args.get('page', default=1, type=int)
        per_page = request.args.get('per_page', default=10, type=int)

        filters = {
            "user_id": request.args.get("user_id"),
            "is_active": request.args.get("active", type=lambda v: v.lower() == 'true' if v else None),
        }

        filters = {k: v for k, v in filters.items() if v is not None}
    
        subscriptions, total = SubscriptionService.get_all( page, per_page, filters)
        return success_response("Subscriptions retrieved", {
                "items": [s.to_dict() for s in subscriptions],
                "total": total,
                "page": page,
                "per_page": per_page,
            })

    @staticmethod
    @jwt_required()
    def get_one(subscription_id):
        subscription = SubscriptionService.get_by_id(subscription_id)
        if not subscription:
            return error_response("Subscription not found", 404)
        return success_response(subscription.to_dict())

    @staticmethod
    @jwt_required()
    def create():
        user_id = get_jwt_identity()
        data = request.get_json()
        try:
            subscription = SubscriptionService.create(user_id, data)
            return success_response("Subscription Created successfully", subscription.to_dict(), 201)
        except Exception as e:
            return error_response(str(e), 400)

    @staticmethod
    @jwt_required()
    def update(subscription_id):
        data = request.get_json()
        updated = SubscriptionService.update(subscription_id, data)
        if not updated:
            return error_response("Subscription not found", 404)
        return success_response("Subscription Updated successfully",updated.to_dict())

    @staticmethod
    @jwt_required()
    def get_active_subscription(user_id):
        subscription = SubscriptionService.get_active_subscription_by_user(user_id)
        if not subscription:
            return error_response("No active subscription found", 404)
        return success_response("Active subscription found", subscription.to_dict())

    @staticmethod
    @jwt_required()
    def check_auto_renew(user_id):
        subscription = SubscriptionService.get_active_subscription_by_user(user_id)
        if not subscription:
            return error_response("No active subscription found", 404)
        return success_response("Auto-renew status retrieved", {
            "auto_renew": subscription.auto_renew
        })

    @staticmethod
    @jwt_required()
    def delete(subscription_id):
        success = SubscriptionService.soft_delete(subscription_id)
        if not success:
            return error_response("Subscription not found", 404)
        return success_response({"message": "Deleted successfully"})
