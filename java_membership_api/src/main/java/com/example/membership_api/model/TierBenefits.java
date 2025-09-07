package com.example.membership_api.model;

import com.example.membership_api.constants.BenefitType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tier_benefits")
@Data
@EqualsAndHashCode(callSuper = true)
public class TierBenefits extends BaseModel {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "tier_id", nullable = false, length = 36)
    private String tierId;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_type", nullable = false)
    private BenefitType benefitType;

    @Column(name = "benefit_value", columnDefinition = "JSON", nullable = false)
    private String benefitValue;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
