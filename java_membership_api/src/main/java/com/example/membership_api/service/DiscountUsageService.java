package com.example.membership_api.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.membership_api.constants.EntityType;
import com.example.membership_api.dao.DiscountUsageDAO;
import com.example.membership_api.model.DiscountUsage;

@Service
public class DiscountUsageService {

    private final DiscountUsageDAO discountUsageDAO;

    public DiscountUsageService(DiscountUsageDAO discountUsageDAO) {
        this.discountUsageDAO = discountUsageDAO;
    }

    public Map<String, Object> getAll(int page, int perPage, String userId, String discountId) {
        List<DiscountUsage> items = discountUsageDAO.getAll(page, perPage, userId, discountId);
        long total = discountUsageDAO.getTotal(userId, discountId);

        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("total", total);
        response.put("page", page);
        response.put("perPage", perPage);
        return response;
    }

    public DiscountUsage getById(String id) {
        return discountUsageDAO.getById(id);
    }

    public List<DiscountUsage> getByUser(int page, int perPage, String userId) {
        return discountUsageDAO.getByUser(userId, page, perPage);
    }

    public long getUserUsageCount(String id) {
        return discountUsageDAO.getUserUsageCount(id);
    }


    public DiscountUsage create(String discountId, String userId, String appliedOn, EntityType entity, Double discountAmount) {
        DiscountUsage usage = new DiscountUsage();
        usage.setDiscountId(discountId);
        usage.setUserId(userId);
        usage.setAppliedOn(appliedOn);
        usage.setEntity(entity);
        usage.setDiscountAmount(discountAmount);
        usage.setUsedAt(LocalDateTime.now());
        return discountUsageDAO.create(usage);
    }
}
