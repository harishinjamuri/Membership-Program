package com.example.membership_api.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.dao.MembershipPlanDAO;
import com.example.membership_api.exception.NotFoundException;
import com.example.membership_api.model.MembershipPlan;

@Service
public class MembershipPlanService {

    @Autowired
    private MembershipPlanDAO membershipPlanDAO;

    public Map<String, Object> getAll(int page, int perPage, Map<String, String> filters) {
        Boolean isActive = filters.containsKey("is_active") 
                   ? Boolean.valueOf(filters.get("is_active")) 
                   : null;

        List<MembershipPlan> items = membershipPlanDAO.getAll(isActive, page, perPage);
        long total = membershipPlanDAO.getTotal(isActive);

        return Map.of(
            "items", items,
            "total", total,
            "page", page,
            "perPage", perPage
        );
    }

    public MembershipPlan getById(String planId) {
        MembershipPlan plan = membershipPlanDAO.getById(planId);
        if (plan == null) {
            throw new NotFoundException("Plan not found");
        }
        return plan;
    }

    public MembershipPlan create(Map<String, Object> plan) {
        System.out.printf("MembershipPlan: %s", plan.toString());
        return membershipPlanDAO.create(plan);
    }

    public MembershipPlan update(String planId, Map<String, Object> updates) {
        MembershipPlan existing = getById(planId);
        System.out.printf("Existing MembershipPlan: %s", existing.toString());
        return membershipPlanDAO.update(existing, updates);
    }

    public boolean softDelete(String planId) {
        return membershipPlanDAO.softDelete(planId);
    }
}
