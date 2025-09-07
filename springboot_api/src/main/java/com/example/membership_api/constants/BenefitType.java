package com.example.membership_api.constants;

public enum BenefitType {
    DISCOUNT("Discount"),
    FREE_SHIPPING("Free_shipping"),
    EARLY_ACCESS("Early_access"),
    PRIORITY_SUPPORT("Priority_support"),
    EXCLUSIVE_PRODUCTS("Exclusive_products");

    private final String value;

    BenefitType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
