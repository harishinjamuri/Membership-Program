from flask import current_app

from app.dao.order_dao import OrderDAO
from app.schemas.order import OrderItemsModel, OrderDiscountsModel
from app.constants import (
    OrderStatus,
    NotificationType,
    EntityType,
    TierType,
    BenefitType,
)

from app.services.order_item_service import OrderItemService
from app.services.user_service import UserService
from app.services.product_service import ProductService
from app.services.tier_benefit_service import TierBenefitService
from app.services.discount_service import DiscountService
from app.services.discount_usage_service import DiscountUsageService
from app.services.notification_service import NotificationService
from app.services.subscription_service import SubscriptionService
from app.services.user_tier_metrics_service import UserTierMetricsService
from pydantic import ValidationError


class OrderService:

    @staticmethod
    def get_all_for_user(page=1, per_page=10, filters=None):
        return OrderDAO.get_all_for_user(page, per_page, filters)

    @staticmethod
    def get_by_id(order_id):
        return OrderDAO.get_by_id(order_id)

    @staticmethod
    def get_all_orders(page=1, per_page=10, filters=None):
        return OrderDAO.get_all_orders(page, per_page, filters)

    @staticmethod
    def create(data):
        return OrderDAO.create(data)

    @staticmethod
    def update(order_id, data):
        try:
            if data.get("items"):
                applies_to_validated = OrderItemsModel(**data.get("items", []))
                data["items"] = applies_to_validated.model_dump()

            if data.get("discounts"):
                applies_to_validated = OrderDiscountsModel(**data.get("discounts", []))
                data["discounts"] = applies_to_validated.model_dump()

        except ValidationError as e:
            raise ValueError(f"Invalid items or discounts format: {e}")
        return OrderDAO.update(order_id, data)

    @staticmethod
    def confirm(user_id, order_id):
        from app.services.membership_tier_service import MembershipTierService

        # Validate user
        user = UserService.get_by_id(user_id)
        if not user:
            raise ValueError("User not found")

        # Validate order
        order = OrderDAO.get_by_id(order_id)

        if order.status != OrderStatus.PENDING:
            raise ValueError(f"Cannot confirm order with status: {order.status}")

        order.status = OrderStatus.CONFIRMED
        current_app.logger.info(f"order: {order.to_dict()}")

        if not order:
            raise ValueError("Order not found")

        # Fetch items
        order_items = OrderItemService.get_all_for_order(order_id)
        current_app.logger.info(f"order_items: {[order_items]}")

        if len(order_items):
            raise ValueError(f"Cannot Confirm Order with out Any items")
        # Optional: Calculate delivery charges
        total_amount = sum(
            [item.total_price - item.total_discount_value for item in order_items]
        )

        current_app.logger.info(f"total_amount: {total_amount}")

        delivery_charges = total_amount * 0.1
        order.delivery_fee_applied = True
        order.delivery_fee = delivery_charges
        current_app.logger.info(f"delivery_charges: {delivery_charges}")

        # --- Handle Discounts ---
        discount_ids = set()

        for item in order_items:
            # --- Update Stock Count Per Item ---
            product = ProductService.get_by_id(item.product_id)
            if not product:
                continue  # Product doesn't exist; skip

            if not product.is_available or product.stock_quantity < item.quantity:
                # Log warning or notify if needed
                continue  # Not enough stock or not available

            product.stock_quantity -= item.quantity
            ProductService.commit(product)

            if item.discount_id:
                discount_ids.add(item.discount_id)
                DiscountUsageService.create(
                    discount_id=item.discount_id,
                    user_id=user_id,
                    applied_on=order.id,
                    entity=EntityType.ORDER_ITEM,
                    discount_amount=item.total_discount_value,
                )

            current_app.logger.info(f"item Name: {item.product_name}")

        current_app.logger.info(f"discount_ids: {discount_ids}")

        for discount_id in discount_ids:
            DiscountService.increment_usage_count(discount_id)

        # --- Send Notification ---
        NotificationService.create(
            {
                "user_id": user_id,
                "notification_type": NotificationType.ORDER_UPDATE,
                "title": "Order Confirmed",
                "message": f"Your order #{order.id} has been confirmed!",
                "details": {
                    "order_id": order.id,
                    "status": OrderStatus.CONFIRMED.value,
                },
            }
        )

        # --- Update Tier Metrics ---
        UserTierMetricsService.create_or_update(
            {"user_id": user_id, "total_spent": total_amount, "order_count": 1}
        )

        # --- Subscription Spend and Saved Update ---
        subscription = SubscriptionService.get_active_subscription_by_user(user_id)

        benefits = TierBenefitService.get_by_tier_id(subscription.tier_id)
        for benefit in benefits:
            if benefit.benefit_type == BenefitType.FREE_SHIPPING:
                order.total_savings += delivery_charges
                order.delivery_fee_applied = False
            elif benefit.benefit_type == BenefitType.DISCOUNT:
                benefit_value = benefit.benefit_value
                discount_amount = round(
                    order.order_total * (benefit_value["percentage"] / 100), 2
                )
                order.total_savings += discount_amount
                DiscountUsageService.create(
                    discount_id=benefit_value["discount_id"],
                    user_id=user_id,
                    applied_on=order.id,
                    entity=EntityType.ORDER,
                    discount_amount=discount_amount,
                )
                DiscountService.increment_usage_count(benefit_value["discount_id"])
                discount_ids.add(benefit_value["discount_id"])

        order.discounts = discount_ids

        subscription.total_spent += total_amount
        subscription.total_saved += order.total_savings
        SubscriptionService.save(subscription)

        # --- Evaluate for Tier Upgrade ---
        tier = MembershipTierService.get_eligible_tiers(user_id)
        if subscription.current_tier < tier.tier_level:
            SubscriptionService.update(
                subscription.subscription_id, {"tier_id": TierType[tier.tier].value}
            )
            NotificationService.create(
                {
                    "user_id": user_id,
                    "notification_type": NotificationType.TIER_UPGRADE,
                    "title": "Tier Upgraded",
                    "message": f"Congratulations You have been upgraded to Tier {tier.tier}.",
                    "details": {},
                }
            )
            current_app.logger.info(
                f"Congratulations You have been upgraded to Tier {tier.tier}.",
            )

        current_app.logger.info(f"order: {order.to_dict()}")
        return OrderService.save(order)

    @staticmethod
    def get_total_orders(user_id):
        return OrderDAO.get_total_orders(user_id)

    @staticmethod
    def get_total_spend(user_id):
        return OrderDAO.get_total_spend(user_id)

    @staticmethod
    def recalculate_order(order_id):
        order = OrderDAO.get_by_id(order_id)
        print(f"Order : {order.to_dict()}")
        if not order:
            return None

        # Get current order items
        order_items = OrderItemService.get_all_for_order(order_id)

        subtotal = sum(item.total_price for item in order_items)
        total_savings = sum(item.discount_applied for item in order_items)

        total_amount = subtotal + (order.delivery_fee or 0) - total_savings

        # Optionally update items list in `order.items` (if using JSON)
        items = [item.id for item in order_items]

        data = {
            "subtotal": subtotal,
            "total_savings": total_savings,
            "total_amount": total_amount,
            "items": items,
        }
        print(f"data: {data}")
        OrderDAO.update(order_id, data)
        return order

    @staticmethod
    def save(order):
        return OrderDAO.save(order)
