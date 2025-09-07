package com.example.membership_api.constants;

public enum SubscriptionStatus {
    ACTIVE("Active"),
    EXPIRED("Expired");

    private final String value;

    SubscriptionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
