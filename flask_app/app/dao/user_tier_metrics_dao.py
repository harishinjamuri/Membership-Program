from datetime import datetime
from app.models.user_tier_metrics import UserTierMetrics
from app.models import db


class UserTierMetricsDAO:

    @staticmethod
    def get_for_user_month(user_id, month_year):
        return UserTierMetrics.query.filter_by(
            user_id=user_id, month_year=month_year
        ).first()

    @staticmethod
    def create_or_update(data):
        user_id = data["user_id"]
        month_year = data.get('month_year') or datetime.utcnow().strftime("%Y-%m")


        utm = UserTierMetricsDAO.get_for_user_month(user_id, month_year)

        if utm:
            utm.order_count += data.get("order_count", 0)
            utm.total_spent += data.get("total_spent", 0)
        else:
            utm = UserTierMetrics(
                user_id=user_id,
                month_year=month_year,
                order_count=data.get("order_count", 0),
                total_spent=data.get("total_spent", 0.0),
            )
            db.session.add(utm)
        db.session.commit()
        return utm

    @staticmethod
    def get_all_metrics():
        return UserTierMetrics.query.order_by(UserTierMetrics.month_year.desc()).all()

    @staticmethod
    def get_top_spenders(month_year, top_n=10):
        return (
            UserTierMetrics.query.filter_by(month_year=month_year)
            .order_by(UserTierMetrics.total_spent.desc())
            .limit(top_n)
            .all()
        )
