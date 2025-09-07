package com.example.membership_api.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.model.UserTierMetrics;
import com.example.membership_api.service.UserTierMetricsService;
import com.example.membership_api.utils.JwtUtil;
import com.example.membership_api.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user-metrics")
public class UserTierMetricsController {

    @Autowired
    private UserTierMetricsService service;

    @GetMapping("/{monthYear}")
    public ResponseEntity<?> getMyMetrics(@PathVariable String monthYear, HttpServletRequest request) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        UserTierMetrics metrics = service.getForUserMonth(userId, monthYear);
        if (metrics == null) {
            return ResponseUtil.error("Metrics not found", 404);
        }
        return ResponseUtil.success("Metrics retrieved", metrics.toMap());
    }

    @PostMapping("/{monthYear}")
    public ResponseEntity<?> createOrUpdate(
        HttpServletRequest request,
            @RequestBody Map<String, Object> payload
    ) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (!payload.containsKey("order_count") || !payload.containsKey("total_spent")) {
            return ResponseUtil.error("Missing fields: order_count, total_spent", 400);
        }

        int orderCount = (int) payload.get("order_count");
        double totalSpent = Double.parseDouble(payload.get("total_spent").toString());

        UserTierMetrics utm = service.createOrUpdate(userId, orderCount, totalSpent);
        return ResponseUtil.success("Metrics updated", utm.toMap());
    }

    @GetMapping
    public ResponseEntity<?> getAllMetrics() {
        List<UserTierMetrics> all = service.getAllMetrics();
        return ResponseUtil.success("All metrics retrieved", all.stream().map(UserTierMetrics::toMap).toList());
    }

    @GetMapping("/top_spenders")
    public ResponseEntity<?> getTopSpenders(
            @RequestParam(required = false) String monthYear,
            @RequestParam(defaultValue = "10") int topN
    ) {
        if (monthYear == null || monthYear.isEmpty()) {
            monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }

        List<UserTierMetrics> top = service.getTopSpenders(monthYear, topN);
        return ResponseUtil.success("Top spenders retrieved", top.stream().map(UserTierMetrics::toMap).toList());
    }
}
