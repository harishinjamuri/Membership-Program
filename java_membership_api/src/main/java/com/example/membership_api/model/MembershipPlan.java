package com.example.membership_api.model;

import com.example.membership_api.constants.PlanType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "membership_plans")
public class MembershipPlan extends BaseModel{

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    @PrePersist
    @PreUpdate
    public void setDurationDaysFromPlanType() {
        if (this.durationDays == 0 && this.planType != null) {
            this.durationDays = this.planType.getDuration();
        }
    }

}
