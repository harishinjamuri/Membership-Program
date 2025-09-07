package com.example.membership_api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.model.Notifications;
import com.example.membership_api.service.NotificationService;
import com.example.membership_api.utils.JwtUtil;
import com.example.membership_api.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "10") int perPage,
            @RequestParam(required = false) Boolean is_read,
            @RequestParam(required = false) String notification_type,
            @RequestParam(required = false) String start_date,
            @RequestParam(required = false) String end_date,
            HttpServletRequest request
    ) {
        // In real app, get userId from auth token or context
        String userId = JwtUtil.extractUserIdFromRequest(request);

        Map<String, Object> filters = new HashMap<>();
        if (is_read != null) filters.put("is_read", is_read);
        if (notification_type != null) filters.put("notification_type", notification_type);
        if (start_date != null) filters.put("start_date", start_date);
        if (end_date != null) filters.put("end_date", end_date);

        List<Notifications> notifications = notificationService.getAllForUser(userId, page, perPage, filters);
        long total = notificationService.countForUser(userId, filters);

        Map<String, Object> response = new HashMap<>();
        response.put("items", notifications);
        response.put("total", total);
        response.put("page", page);
        response.put("per_page", perPage);

        return ResponseEntity.ok(ResponseUtil.success("Notifications retrieved", response));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<?> markRead(@PathVariable String notificationId) {
        Notifications updated = notificationService.markAsRead(notificationId);
        if (updated == null) {
            return ResponseEntity.status(404).body(ResponseUtil.error("Notification not found", 0));
        }
        return ResponseEntity.ok(ResponseUtil.success("Notification marked as read", updated));
    }

    @PutMapping("/{notificationId}/readall")
    public ResponseEntity<?> markAllAsRead(HttpServletRequest request) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ResponseUtil.success("All notifications marked as read"));
    }
}
