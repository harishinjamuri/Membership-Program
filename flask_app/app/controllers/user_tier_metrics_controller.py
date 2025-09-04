from datetime import datetime
from flask import request
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.services.user_tier_metrics_service import UserTierMetricsService
from app.utils.response import success_response, error_response


class UserTierMetricsController:

    @staticmethod
    @jwt_required()
    def get_my_metrics(month_year):
        user_id = get_jwt_identity()
        metrics = UserTierMetricsService.get_for_user_month(user_id, month_year)
        if not metrics:
            return error_response("Metrics not found", 404)
        return success_response("Metrics retrieved", metrics.to_dict())

    @staticmethod
    @jwt_required()
    def create_or_update(month_year):
        user_id = get_jwt_identity()
        data = request.get_json()
        required_fields = ["order_count", "total_spent"]

        # Validate required fields
        missing = [f for f in required_fields if f not in data]
        if missing:
            return error_response(f"Missing fields: {', '.join(missing)}", 400)

        # Validate month format
        try:
            datetime.strptime(month_year, "%Y-%m")
        except ValueError:
            return error_response("Invalid month format. Use YYYY-MM.", 400)

        data.update({"user_id": user_id, "month_year": month_year})
        utm = UserTierMetricsService.create_or_update(data)
        return success_response("Metrics updated", utm.to_dict())

    @staticmethod
    @jwt_required()
    def get_user_metrics():
        """Get metrics for all users (admin/reporting purpose)."""
        all_metrics = UserTierMetricsService.get_all_metrics()
        return success_response(
            "All metrics retrieved", [m.to_dict() for m in all_metrics]
        )

    @staticmethod
    @jwt_required()
    def get_top_spenders():
        """Get top N spenders for a given month. Defaults to current month."""
        month_year = request.args.get("month_year")
        top_n = int(request.args.get("top_n", 10))
        if not month_year:
            month_year = datetime.utcnow().strftime("%Y-%m")

        top_spenders = UserTierMetricsService.get_top_spenders(month_year, top_n)
        return success_response(
            "Top spenders retrieved", [m.to_dict() for m in top_spenders]
        )
