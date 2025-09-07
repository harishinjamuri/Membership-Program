package com.example.membership_api.constants;

public enum PlanType {
    MONTHLY(30),
    QUARTERLY(90),
    YEARLY(365);

    private final int days;

    PlanType(int days) {
        this.days = days;
    }

    public int getDuration() {
        return days;
    }
}
