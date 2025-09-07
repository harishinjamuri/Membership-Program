package com.example.membership_api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class DiscountBenefit {
    private float percent;
    @JsonAlias("discount_id")
    private String discountId;
}