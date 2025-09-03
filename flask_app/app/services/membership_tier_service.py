from datetime import datetime
from app.dao.membership_tier_dao import MembershipTierDAO
from app.services.user_tier_metrics_service import UserTierMetricsService
from app.services.order_service import OrderService

class MembershipTierService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return MembershipTierDAO.get_all(page=page, per_page=per_page, filters=filters or {})

    @staticmethod
    def get_by_id(tier_id):
        return MembershipTierDAO.get_by_id(tier_id)

    @staticmethod
    def create(data):
        return MembershipTierDAO.create(data)

    @staticmethod
    def update(tier_id, data):
        return MembershipTierDAO.update(tier_id, data)

    @staticmethod
    def soft_delete(tier_id):
        return MembershipTierDAO.soft_delete(tier_id)
    
    @staticmethod
    def get_eligible_tiers(user_id):

        current_month_year = datetime.now().strftime("%Y-%m")
        monthly_metrics = UserTierMetricsService.get_for_user_month(
            user_id=user_id,
            month_year=current_month_year
        )

        min_monthly_orders = monthly_metrics.order_count if monthly_metrics else 0
        min_monthly_spend = monthly_metrics.total_spent if monthly_metrics else 0
        
        # users orders, spend from orders
        total_orders = OrderService.get_total_orders(user_id)
        total_spend = OrderService.get_total_spend(user_id)

        data = {
           "monthly_orders": min_monthly_orders,
            "monthly_spend": min_monthly_spend,
            "total_orders": total_orders,
            "total_spend": total_spend,
        }
        
        return MembershipTierDAO.get_eligible_tier(data)
    