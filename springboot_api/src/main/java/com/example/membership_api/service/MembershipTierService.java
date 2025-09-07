package com.example.membership_api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.dao.MembershipTierDAO;
import com.example.membership_api.dao.OrderDAO;
import com.example.membership_api.exception.NotFoundException;
import com.example.membership_api.model.MembershipTier;
import com.example.membership_api.model.UserTierMetrics;


@Service
public class MembershipTierService {

    @Autowired
    private MembershipTierDAO dao;

    @Autowired
    private UserTierMetricsService userTierMetricsService;

    @Autowired
    private OrderDAO orderDAO;

    public Map<String, Object> getAll(int page, int perPage, Map<String, Object> filters) {
        List<MembershipTier> items = dao.getAll(page, perPage, filters);
        long total = dao.getTotal(filters);
        return Map.of("items", items, "total", total, "page", page, "perPage", perPage);
    }

    public MembershipTier getById(String id) {
        MembershipTier tier = dao.getById(id);
        if (tier == null) throw new NotFoundException("Tier not found");
        return tier;
    }

    public List<MembershipTier> getActiveByTier(int tier){
        return dao.getActiveByTier(tier);
    }

    public MembershipTier create(Map<String, Object> tier) {
        return dao.create(tier);
    }

    public MembershipTier update(String id, Map<String, Object> updates) {
        MembershipTier existing = getById(id);
        return dao.update(existing, updates);
    }

    public boolean softDelete(String id) {
        return dao.softDelete(id);
    }

    public MembershipTier getEligibleTier(String userId) {
        String currentMonthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        UserTierMetrics monthlyMetrics = userTierMetricsService.getForUserMonth(userId, currentMonthYear);

        int monthlyOrders = monthlyMetrics != null ? monthlyMetrics.getOrderCount() : 0;
        double monthlySpend = monthlyMetrics != null ? monthlyMetrics.getTotalSpent() : 0.0;

        long totalOrders = orderDAO.getTotalOrders(userId);
        double totalSpend = orderDAO.getTotalSpend(userId);

        Map<String, Object> data = new HashMap<>();
        data.put("monthly_orders", monthlyOrders);
        data.put("monthly_spend", monthlySpend);
        data.put("total_orders", totalOrders);
        data.put("total_spend", totalSpend);
        System.out.printf("getEligibleTier: %s", data.toString());
        return dao.getEligibleTier(data);
    }
}
