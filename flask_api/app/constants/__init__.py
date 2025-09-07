from enum import Enum


class UserType(Enum):
    ADMIN = "Admin"
    USER = "User"


class UserStatus(Enum):
    ACTIVE = "Active"
    INACTIVE = "InActive"
    SUSPENDED = "Suspended"


class SubscriptionStatus(Enum):
    ACTIVE = "Active"
    EXPIRED = "Expired"


class TierType(Enum):
    BRONZE = 1
    SILVER = 2
    GOLD = 3
    PLATINUM = 4


class PlanType(Enum):
    """Enumeration of subscription plan types."""

    MONTHLY = 30
    QUARTERLY = 90
    YEARLY = 365


class DiscountType(Enum):
    PERCENTAGE = "Percentage"
    FIXED_AMOUNT = "Fixed_amount"
    FREE_SHIPPING = "Free_shipping"


class BenefitType(Enum):
    DISCOUNT = "Discount"
    FREE_SHIPPING = "Free_shipping"
    EARLY_ACCESS = "Early_access"
    PRIORITY_SUPPORT = "Priority_support"
    EXCLUSIVE_PRODUCTS = "Exclusive_products"


class OrderStatus(Enum):
    """Enumeration of order statuses."""

    PENDING = "Pending"
    CONFIRMED = "Confirmed"
    PROCESSING = "Processing"
    SHIPPED = "Shipped"
    DELIVERED = "Delivered"
    CANCELLED = "Cancelled"


class NotificationType(Enum):
    ORDER_UPDATE = "OrderUpdate"
    DISCOUNT_ALERT = "DiscountAlert"
    SUBSCRIPTION_STATUS = "SubscriptionStatus"
    TIER_UPGRADE = "TierUpgrade"
    TIER_DOWNGRADE = "TierDowngrade"
    MEMBERSHIP_EXPIRY = "MembershipExpiry"
    NEW_PRODUCT = "NewProduct"
    ACCOUNT_ACTIVITY = "AccountActivity"
    PASSWORD_CHANGED = "PasswordChanged"
    GENERAL_ANNOUNCEMENT = "GeneralAnnouncement"


class EntityType(Enum):
    ORDER = "Order"
    ORDER_ITEM = "OrderItem"
    DELIVERY = "Delivery"


class DiscountApplicableType(Enum):
    Product = "product"
    Category = "category"
    Tier = "tier"
    Plan = "plan"
