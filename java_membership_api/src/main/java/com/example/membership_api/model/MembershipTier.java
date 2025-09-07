package com.example.membership_api.model;

import com.example.membership_api.constants.TierType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "membership_tiers")
@DynamicUpdate
public class MembershipTier extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TierType tier;

    @Column(name = "tier_level", nullable = false)
    private Integer tierLevel = 1;

    @Column(name = "min_monthly_orders", nullable = false)
    private Integer minMonthlyOrders;

    @Column(name = "min_monthly_spend", nullable = false)
    private Double minMonthlySpend;

    @Column(name = "min_total_orders", nullable = false)
    private Integer minTotalOrders;

    @Column(name = "min_total_spend", nullable = false)
    private Double minTotalSpend;

    @PrePersist
    @PreUpdate
    public void syncTierLevel() {
        if (this.tier != null) {
            this.tierLevel = tier.getLevel();
        }
    }
}
