package com.example.membership_api.utils;

import com.example.membership_api.constants.BenefitType;
import com.example.membership_api.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BenefitValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String validateAndNormalize(String benefitTypeStr, String benefitValueJson) {
        BenefitType type;
        try {
            type = BenefitType.valueOf(benefitTypeStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid benefit_type: " + benefitTypeStr);
        }

        try {
            switch (type) {
                case DISCOUNT:
                    objectMapper.readValue(benefitValueJson, DiscountBenefit.class);
                    break;
                case FREE_SHIPPING:
                    objectMapper.readValue(benefitValueJson, FreeShippingBenefit.class);
                    break;
                case EARLY_ACCESS:
                    objectMapper.readValue(benefitValueJson, EarlyAccessBenefit.class);
                    break;
                case PRIORITY_SUPPORT:
                    objectMapper.readValue(benefitValueJson, PrioritySupportBenefit.class);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported benefit_type: " + type);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid benefit_value for type " + type + ": " + e.getMessage());
        }

        return benefitValueJson; // optionally, re-serialize normalized object here
    }
}
