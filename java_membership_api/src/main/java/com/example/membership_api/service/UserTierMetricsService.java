package com.example.membership_api.service;

import com.example.membership_api.dao.UserTierMetricsDAO;
import com.example.membership_api.model.UserTierMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTierMetricsService {

    @Autowired
    private UserTierMetricsDAO dao;

    public UserTierMetrics getForUserMonth(String userId, String monthYear) {
        return dao.getForUserMonth(userId, monthYear);
    }

    public UserTierMetrics createOrUpdate(String userId, int orderCount, double totalSpent) {
        return dao.createOrUpdate(userId, orderCount, totalSpent);
    }

    public List<UserTierMetrics> getAllMetrics() {
        return dao.getAllMetrics();
    }

    public List<UserTierMetrics> getTopSpenders(String monthYear, int topN) {
        return dao.getTopSpenders(monthYear, topN);
    }
}
