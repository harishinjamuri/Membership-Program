package com.example.membership_api.model;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_tier_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
public class UserTierMetrics extends BaseModel {

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "month_year", nullable = false, length = 7)
    private String monthYear; // Format: YYYY-MM

    @Column(name = "order_count", nullable = false)
    private int orderCount;

    @Column(name = "total_spent", nullable = false)
    private double totalSpent;

    @Transient
    public String getMonthName() {
        try {
            return java.time.LocalDate.parse(this.monthYear + "-01").format(java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy"));
        } catch (Exception e) {
            return this.monthYear;
        }
    }

    @Transient
    public double getAverageOrderValue() {
        return orderCount == 0 ? 0.0 : Math.round(totalSpent / orderCount * 100.0) / 100.0;
    }
}
