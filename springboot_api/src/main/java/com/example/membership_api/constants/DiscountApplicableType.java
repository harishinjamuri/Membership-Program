package com.example.membership_api.constants;

public enum DiscountApplicableType {
    PRODUCT("product"),
    CATEGORY("category"),
    TIER("tier"),
    PLAN("plan");

    private final String value;

    DiscountApplicableType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
