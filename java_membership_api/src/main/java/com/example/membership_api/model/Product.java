package com.example.membership_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "product")
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseModel{

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Column(name = "discount_id")
    private String discountId;

    @Column(nullable = false)
    private Double price = 0.0;

    @Column(name = "is_membership_exclusive", nullable = false)
    private Boolean isMembershipExclusive = false;

    @Column(name = "min_tier_required", nullable = false)
    private Integer minTierRequired = 1;

    @Transient
    public boolean isAvailable() {
        return stockQuantity != null && stockQuantity > 0;
    }
}
