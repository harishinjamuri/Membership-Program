from flask import Blueprint
from app.controllers.notification_controller import NotificationController

notification_bp = Blueprint('notification', __name__, url_prefix='/notifications')

notification_bp.get('/me')(NotificationController.get_my_notifications)
notification_bp.put('/<notification_id>/read')(NotificationController.mark_read)
notification_bp.put('/<notification_id>/readall')(NotificationController.mark_all_as_read)
