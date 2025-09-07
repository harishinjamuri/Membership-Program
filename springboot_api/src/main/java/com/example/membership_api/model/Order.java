package com.example.membership_api.model;

import java.util.List;
import java.util.Map;

import com.example.membership_api.constants.OrderStatus;
import com.example.membership_api.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseModel {

    @Column(nullable = false, length = 36)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private double totalAmount = 0.0;

    @Column(nullable = false)
    private double totalSavings = 0.0;

    @Column(nullable = false)
    private double subtotal = 0.0;

    @Column(nullable = false)
    private String deliveryAddress;

    @Column(nullable = false)
    private double deliveryFee = 0.0;

    @Column(nullable = false)
    private boolean deliveryFeeApplied = true;

    @Column(columnDefinition = "JSON", nullable = false)
    private String items="[]";  

    @Column(columnDefinition = "JSON", nullable = false)
    private String discounts="[]"; 

    // Helper method
    public int getOrderItemCount() {
        try {
            List<String> itemList = JsonUtils.parseJson(items, new TypeReference<List<String>>() {});
            // List<Map<String, Object>> itemList = JsonUtils.parseJson(items);
            System.out.println("Parsed item count: " + itemList.size());
            return itemList.size();
        } catch (Exception e) {
            System.err.println("Failed to parse items: " + e.getMessage());
            return 0;
        }
    }
}
