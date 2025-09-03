from flask import Blueprint
from app.controllers.user_tier_metrics_controller import UserTierMetricsController

utm_bp = Blueprint('user_tier_metrics', __name__, url_prefix='/user-metrics')

utm_bp.get('/')(UserTierMetricsController.get_user_metrics)
utm_bp.get('/<month_year>')(UserTierMetricsController.get_my_metrics)
utm_bp.get('/top_spenders')(UserTierMetricsController.get_top_spenders)
utm_bp.post('/<month_year>')(UserTierMetricsController.create_or_update)