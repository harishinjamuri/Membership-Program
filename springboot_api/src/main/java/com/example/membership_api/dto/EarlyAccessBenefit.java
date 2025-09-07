package com.example.membership_api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class EarlyAccessBenefit {
    private boolean enabled;
    @JsonAlias("hours_before")
    private int hoursBefore;
}