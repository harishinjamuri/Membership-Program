package com.example.membership_api.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.membership_api.constants.DiscountType;
import com.example.membership_api.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name = "discount")
@EqualsAndHashCode(callSuper = true)
public class Discount extends BaseModel {

    @Column(nullable = false, length = 36)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private double value;

    @Column(name = "applies_to", columnDefinition = "JSON", nullable = false)
    private String appliesTo;

    @Column(nullable = false, name = "start_date")
    private LocalDateTime startDate;

    @Column(nullable = false, name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(nullable = false, name = "usage_count")
    private int usageCount = 0;

    @Column(nullable = false, name = "min_order_value")
    private double minOrderValue = 0;

    @Column(nullable = false, name = "max_discount_amount")
    private double maxDiscountAmount = 0;

    @Column(nullable = false, name = "min_tier")
    private int minTier;

    // --- Computed properties ---

    @Transient
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endDate);
    }

    @Transient
    public boolean isUpcoming() {
        return LocalDateTime.now().isBefore(startDate);
    }

    @Transient
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return isActive && !now.isBefore(startDate) && !now.isAfter(endDate);
    }

    @Transient
    public String getAppliesToType() {
        Map<String, Object> json = parseAppliesToJson();
        return json.getOrDefault("type", "").toString();
    }

    @Transient
    public List<String> getApplicableIds() {
        Map<String, Object> json = parseAppliesToJson();
        Object ids = json.get("ids");

        if (ids instanceof List<?> idList) {
            List<String> stringIds = new java.util.ArrayList<>();
            for (Object item : idList) {
                if (item instanceof String str) {
                    stringIds.add(str);
                }
            }
            return stringIds;
        }

        return Collections.emptyList();
    }


    @Transient
    public boolean isFirstTimeUserOnly() {
        Map<String, Object> json = parseAppliesToJson();
        Object val = json.get("first_time_user_only");
        return val instanceof Boolean && (Boolean) val;
    }

    @Transient
    public boolean isAutoApply() {
        Map<String, Object> json = parseAppliesToJson();
        Object val = json.get("auto_apply");
        return val instanceof Boolean && (Boolean) val;
    }

    // --- Helper method to parse JSON ---
    private Map<String, Object> parseAppliesToJson() {
        try {
            return JsonUtils.fromJson(appliesTo, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return Collections.emptyMap(); // fallback to prevent crashes
        }
    }

}
