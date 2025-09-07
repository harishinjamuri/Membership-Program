package com.example.membership_api.constants;

public enum EntityType {
    ORDER("Order"),
    ORDER_ITEM("OrderItem"),
    DELIVERY("Delivery");

    private final String value;

    EntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
