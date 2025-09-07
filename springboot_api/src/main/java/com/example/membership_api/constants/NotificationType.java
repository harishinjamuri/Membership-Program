package com.example.membership_api.constants;

public enum NotificationType {
    ORDER_UPDATE("OrderUpdate"),
    DISCOUNT_ALERT("DiscountAlert"),
    SUBSCRIPTION_STATUS("SubscriptionStatus"),
    TIER_UPGRADE("TierUpgrade"),
    TIER_DOWNGRADE("TierDowngrade"),
    MEMBERSHIP_EXPIRY("MembershipExpiry"),
    NEW_PRODUCT("NewProduct"),
    ACCOUNT_ACTIVITY("AccountActivity"),
    PASSWORD_CHANGED("PasswordChanged"),
    GENERAL_ANNOUNCEMENT("GeneralAnnouncement");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
