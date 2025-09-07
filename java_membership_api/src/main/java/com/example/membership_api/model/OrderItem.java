package com.example.membership_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@Table(name = "order_item")
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends  BaseModel{

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "discount_applied", nullable = false)
    private Boolean discountApplied = false;

    @Column(name = "discount_value", nullable = false)
    private Double discountValue = 0.0;

    @Column(name = "discount_id", nullable = true)
    private String discountId;

    @Column(name = "total_discount_value", nullable = false)
    private Double totalDiscountValue = 0.0;

    public Double getOriginalTotal() {
        return Math.round(unitPrice * quantity * 100.0) / 100.0;
    }

    public Double getEffectiveTotal() {
        double discount = (discountApplied && discountValue != null) ? discountValue : 0.0;
        return Math.round((getOriginalTotal() - discount) * 100.0) / 100.0;
    }
}
