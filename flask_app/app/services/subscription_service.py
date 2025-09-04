from datetime import timedelta
from app.dao.subscription_dao import SubscriptionDAO
from app.dao.membership_tier_dao import MembershipTierDAO
from app.dao.membership_plans_dao import MembershipPlansDAO
from app.services.tier_benefit_service import TierBenefitService
from flask import current_app

from app.constants import TierType
from app.utils import generate_subscription_number, calculate_end_date_from_current


class SubscriptionService:

    @staticmethod
    def get_all(page=1, per_page=10, filters=None):
        return SubscriptionDAO.get_all(page, per_page, filters)

    @staticmethod
    def get_by_id(subscription_id):
        return SubscriptionDAO.get_by_id(subscription_id)

    @staticmethod
    def create(user_id, data):
        plan_id = data["plan_id"]
        data["user_id"] = user_id
        current_app.logger.info(
            f"TierType:  {TierType.BRONZE.name} {TierType.BRONZE.value}"
        )
        bronze_tiers = MembershipTierDAO.get_active_by_tier(TierType.BRONZE.value)
        current_app.logger.info(
            f"bronze_tiers:  {[ tier.to_dict() for tier in bronze_tiers]} "
        )
        if len(bronze_tiers) > 0:
            default_tier_id = bronze_tiers[0].id

            benefits = TierBenefitService.get_by_tier_id(default_tier_id)
            serialized_benefits = [b.id for b in benefits]

            plan_details = MembershipPlansDAO.get_by_id(plan_id).to_dict()
            current_app.logger.info(
                f"default_tier_id : {default_tier_id} benefits : {benefits} plan_details: {plan_details}"
            )
            start_date, end_date = calculate_end_date_from_current(
                plan_details["duration_days"]
            )
            current_app.logger.info(f"start_date: {start_date} end_date: {end_date}")
            data["tier_id"] = default_tier_id
            data["current_tier"] = TierType.BRONZE.value
            data["benefits"] = serialized_benefits
            data["subscription_number"] = generate_subscription_number().upper()
            data["start_date"] = start_date
            data["end_date"] = end_date
            data["total_saved"] = 0
            data["total_spent"] = 0

        current_app.logger.info(f"Data: {data}")
        return SubscriptionDAO.create(data)

    @staticmethod
    def update(subscription_id, data):

        if data.get("tier_id"):
            benefits = TierBenefitService.get_by_tier_id(data.get("tier_id"))
            serialized_benefits = [b.id for b in benefits]
            data["benefits"] = serialized_benefits

        if data.get("plan_id"):
            plan_id = data["plan_id"]
            plan_details = MembershipPlansDAO.get_by_id(plan_id).to_dict()
            start_date, end_date = calculate_end_date_from_current(
                plan_details["duration_days"]
            )
            current_app.logger.info(f"start_date: {start_date} end_date: {end_date}")
            data["start_date"] = start_date
            data["end_date"] = end_date

        return SubscriptionDAO.update(subscription_id, data)

    @staticmethod
    def save(subscription):
        return SubscriptionDAO.save(subscription)

    @staticmethod
    def get_active_subscription_by_user(user_id):
        return SubscriptionDAO.get_active_by_user(user_id)

    @staticmethod
    def extend_subscription(subscription_id, extend_days=None, additional_data=None):
        subscription = SubscriptionDAO.get_by_id(subscription_id)
        if not subscription:
            return None

        if subscription.is_expired and not subscription.is_active:
            return None

        if extend_days:
            subscription.end_date += timedelta(days=int(extend_days))

        allowed_fields = ["auto_renew", "tier_id", "plan_id", "benefits"]
        for field in allowed_fields:
            if field in additional_data:
                setattr(subscription, field, additional_data[field])

        return SubscriptionDAO.save(subscription)

    @staticmethod
    def soft_delete(subscription_id):
        return SubscriptionDAO.soft_delete(subscription_id)
