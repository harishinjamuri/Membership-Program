package com.example.membership_api.model;

import com.example.membership_api.constants.EntityType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "discount_usage")
@EqualsAndHashCode(callSuper = true)
public class DiscountUsage extends BaseModel {

    @Column(name = "discount_id", nullable = false, length = 36)
    private String discountId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "applied_on", nullable = false, length = 36)
    private String appliedOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity", nullable = false)
    private EntityType entity;

    @Column(name = "discount_amount", nullable = false)
    private Double discountAmount;

    @Column(name = "used_at", nullable = false)
    private LocalDateTime usedAt;

}
