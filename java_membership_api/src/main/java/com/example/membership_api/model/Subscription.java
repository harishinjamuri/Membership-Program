package com.example.membership_api.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.example.membership_api.constants.SubscriptionStatus;
import com.example.membership_api.constants.TierType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "subscription")
@Data
@EqualsAndHashCode(callSuper = true)
public class Subscription extends BaseModel {

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "plan_id", nullable = false, length = 36)
    private String planId;

    @Column(name = "tier_id", nullable = false, length = 36)
    private String tierId;

    @Column(name = "current_tier")
    private Integer currentTier = TierType.BRONZE.getLevel();

    @Column(name = "benefits", columnDefinition = "JSON", nullable = false)
    private String benefits;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "auto_renew")
    private Boolean autoRenew = true;

    @Column(name = "subscription_number", nullable = false, length = 36)
    private String subscriptionNumber;

    @Column(name = "total_saved")
    private Double totalSaved = 0.0;

    @Column(name = "total_spent")
    private Double totalSpent = 0.0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // --- Custom Logic ---

    public String getStatus() {
        return isExpired() ? SubscriptionStatus.EXPIRED.getValue() : SubscriptionStatus.ACTIVE.getValue();
    }

    public long getDaysRemaining() {
        if (endDate == null || endDate.isBefore(LocalDateTime.now())) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endDate);
    }

    public boolean isExpired() {
        return endDate != null && LocalDateTime.now().isAfter(endDate);
    }

    public boolean isCurrentlyActive() {
        return Boolean.TRUE.equals(isActive) && !isExpired();
    }

    public long getDurationInDays() {
        return startDate != null && endDate != null
                ? ChronoUnit.DAYS.between(startDate, endDate)
                : 0;
    }

    public int getProgressPercentage() {
        if (startDate == null || endDate == null) return 0;

        long totalDays = getDurationInDays();
        long usedDays = ChronoUnit.DAYS.between(startDate, LocalDateTime.now());

        if (totalDays <= 0) {
            return isExpired() ? 100 : 0;
        }

        return (int) Math.min(100, Math.max(0, (usedDays * 100) / totalDays));
    }

}
