package com.example.membership_api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class FreeShippingBenefit {
    private boolean enabled;
    
    @JsonAlias("min_order_value")
    private float minOrderValue;
}