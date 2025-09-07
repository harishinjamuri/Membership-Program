package com.example.membership_api.service;

import com.example.membership_api.dao.NotificationDAO;
import com.example.membership_api.model.Notifications;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;

    public NotificationService(NotificationDAO notificationDAO) {
        this.notificationDAO = notificationDAO;
    }

    public List<Notifications> getAllForUser(String userId, int page, int perPage, Map<String, Object> filters) {
        return notificationDAO.getAllForUser(userId, page, perPage, filters);
    }

    public long countForUser(String userId, Map<String, Object> filters) {
        return notificationDAO.countForUser(userId, filters);
    }

    public Notifications create(Notifications notification) {
        return notificationDAO.create(notification);
    }

    public Notifications markAsRead(String notificationId) {
        return notificationDAO.markAsRead(notificationId);
    }

    public Notifications getById(String notificationId) {
        return notificationDAO.getById(notificationId);
    }

    public void markAllAsRead(String userId) {
        notificationDAO.markAllAsRead(userId);
    }
}
