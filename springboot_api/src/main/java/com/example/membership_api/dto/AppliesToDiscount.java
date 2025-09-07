package com.example.membership_api.dto;

import java.util.List;

import lombok.Data;

@Data
public class AppliesToDiscount {
    private String type; 
    private List<String> ids;
}