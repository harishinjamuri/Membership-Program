package com.example.membership_api.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.membership_api.constants.BenefitType;
import com.example.membership_api.dao.TierBenefitDAO;
import com.example.membership_api.model.TierBenefits;
import com.example.membership_api.utils.BenefitValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TierBenefitService {

    private static final Logger logger = LoggerFactory.getLogger(TierBenefitService.class);

    private final TierBenefitDAO dao;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public TierBenefitService(TierBenefitDAO dao) {
        this.dao = dao;
    }

    public List<TierBenefits> getAll(boolean onlyActive, int page, int perPage) {
        return dao.getAll(onlyActive, page, perPage);
    }

    public TierBenefits getById(String id) {
        return dao.getById(id);
    }

    public List<TierBenefits> getByTierId(String tierId) {
        return dao.getByTierId(tierId);
    }

    public TierBenefits create(Map<String, Object> benefitData) {
        try {
            String benefitTypeStr = (String) benefitData.get("benefit_type");

            Object rawValue = benefitData.get("benefit_value");
            String benefitValueJson = (rawValue instanceof String)
                    ? (String) rawValue
                    : objectMapper.writeValueAsString(rawValue);

            String validated = BenefitValidator.validateAndNormalize(benefitTypeStr, benefitValueJson);

            TierBenefits benefit = new TierBenefits();
            benefit.setTierId( (String) benefitData.get("tier_id"));
            benefit.setBenefitType(BenefitType.valueOf(benefitTypeStr.toUpperCase()));
            benefit.setBenefitValue(validated);
            benefit.setDescription((String) benefitData.get("description"));
            benefit.setSortOrder(((Number) benefitData.get("sort_order")).intValue());
            System.out.printf("benefit: %s", benefit);
            return dao.create(benefit);
        } catch (Exception e) {
            logger.error("Error during benefit creation", e);
            throw new IllegalArgumentException("Invalid benefit_type or benefit_value", e);
        }
    }

    public TierBenefits update(String id, Map<String, Object>  updates) {
        TierBenefits existing = dao.getById(id);
        if (existing == null) return null;

        try {
            BenefitType benefitType = existing.getBenefitType();
            if (updates.containsKey("benefit_type") && updates.get("benefit_type") != null) {
                String benefitTypeStr = ((String) updates.get("benefit_type")).toUpperCase();
                benefitType = BenefitType.valueOf(benefitTypeStr);
            }

            String benefitValue;
            if (updates.containsKey("benefit_value") && updates.get("benefit_value") != null) {
                Object rawValue = updates.get("benefit_value");

                // Serialize to JSON string if it's a Map or object
                if (rawValue instanceof String) {
                    benefitValue = (String) rawValue;
                } else {
                    benefitValue = objectMapper.writeValueAsString(rawValue);
                }
            } else {
                benefitValue = existing.getBenefitValue();
            }

            String validated = BenefitValidator.validateAndNormalize(benefitType.name(), benefitValue);
            existing.setBenefitType(benefitType);
            existing.setBenefitValue(validated);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid benefit_value during update", e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to process benefit_value", e);
            throw new RuntimeException("Invalid benefit_value format", e);
        }

        return dao.update(existing, updates);
    }


    public boolean softDelete(String id) {
        return dao.softDelete(id);
    }
}
