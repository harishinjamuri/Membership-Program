package com.example.membership_api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.model.Subscription;
import com.example.membership_api.service.SubscriptionService;
import com.example.membership_api.utils.JwtUtil;
import com.example.membership_api.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false, name = "user_id") String userId,
            @RequestParam(required = false, name = "active") Boolean active
    ) {
        Map<String, Object> filters = new HashMap<>();
        if (userId != null) filters.put("user_id", userId);
        if (active != null) filters.put("is_active", active);

        Map<String, Object> data = subscriptionService.getAll(page, perPage, filters);
        return ResponseUtil.success("Subscriptions retrieved", data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        Subscription subscription = subscriptionService.getById(id);
        if (subscription == null) {
            return ResponseUtil.error("Subscription not found", 404);
        }
        return ResponseUtil.success("Subscription found", subscription);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> data, HttpServletRequest request) {
        try {
            String userId = JwtUtil.extractUserIdFromRequest(request);
            Subscription created = subscriptionService.create(userId, data);
            return ResponseUtil.success("Subscription created", created);
        } catch (Exception e) {
            return ResponseUtil.error("Failed to create subscription: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        Subscription updated = subscriptionService.update(id, data);
        if (updated == null) {
            return ResponseUtil.error("Subscription not found", 404);
        }
        return ResponseUtil.success("Subscription updated", updated);
    }

    @GetMapping("/active/{userId}")
    public ResponseEntity<?> getActiveSubscription(@PathVariable String userId) {
        Subscription subscription = subscriptionService.getActiveSubscriptionByUser(userId);
        if (subscription == null) {
            return ResponseUtil.error("No active subscription found", 404);
        }
        return ResponseUtil.success("Active subscription found", subscription);
    }

    @GetMapping("/auto-renew/{userId}")
    public ResponseEntity<?> checkAutoRenew(@PathVariable String userId) {
        Subscription subscription = subscriptionService.getActiveSubscriptionByUser(userId);
        if (subscription == null) {
            return ResponseUtil.error("No active subscription found", 404);
        }
        return ResponseUtil.success("Auto-renew status", Map.of("auto_renew", subscription.getAutoRenew()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = subscriptionService.softDelete(id);
        if (!deleted) {
            return ResponseUtil.error("Subscription not found", 404);
        }
        return ResponseUtil.success("Subscription deleted");
    }
}
