from app.dao.user_tier_metrics_dao import UserTierMetricsDAO

class UserTierMetricsService:

    @staticmethod
    def get_for_user_month(user_id, month_year):
        return UserTierMetricsDAO.get_for_user_month(user_id, month_year)

    @staticmethod
    def create_or_update(data):
        return UserTierMetricsDAO.create_or_update(data)

    @staticmethod
    def get_all_metrics():
        return UserTierMetricsDAO.get_all_metrics()

    @staticmethod
    def get_top_spenders(month_year, top_n=10):
        return UserTierMetricsDAO.get_top_spenders(month_year, top_n)