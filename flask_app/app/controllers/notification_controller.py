from flask import request
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.services.notification_service import NotificationService
from app.utils.response import success_response, error_response

class NotificationController:

    @staticmethod
    @jwt_required()
    def get_my_notifications():
        user_id = get_jwt_identity()
        page = request.args.get('page', default=1, type=int)
        per_page = request.args.get('per_page', default=10, type=int)
        
        filters = {
            "is_read": request.args.get("is_read", type=lambda v: v.lower() == 'true' if v else None),
            "notification_type": request.args.get("notification_type"),
            "start_date": request.args.get("start_date"), #YYYY-MM-DD
            "end_date": request.args.get("end_date"),
        }

        filters = {k: v for k, v in filters.items() if v is not None}
        
        notifications, total = NotificationService.get_all_for_user( user_id, page, per_page, filters)
        return success_response("Notifications retrieved", {
                "items": [n.to_dict() for n in notifications],
                "total": total,
                "page": page,
                "per_page": per_page,
            })

    @staticmethod
    @jwt_required()
    def mark_read(notification_id):
        updated = NotificationService.mark_as_read(notification_id)
        if not updated:
            return error_response("Notification not found", 404)
        return success_response("Notification marked as read", updated.to_dict())

    @staticmethod
    @jwt_required()
    def mark_all_as_read():
        user_id = get_jwt_identity()
        NotificationService.mark_all_as_read(user_id)
        return success_response("All notifications marked as read")