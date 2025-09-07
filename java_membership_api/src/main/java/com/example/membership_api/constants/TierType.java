package com.example.membership_api.constants;

public enum TierType {
    BRONZE(1),
    SILVER(2),
    GOLD(3),
    PLATINUM(4);

    private final int level;

    TierType(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
