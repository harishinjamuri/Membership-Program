package com.example.membership_api.dao;

import com.example.membership_api.model.Notifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Repository
public class NotificationDAO {

    @PersistenceContext
    private EntityManager em;

    public List<Notifications> getAllForUser(String userId, int page, int perPage, Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT n FROM Notifications n WHERE n.userId = :userId");

        if (filters != null) {
            if (filters.containsKey("is_read")) {
                queryBuilder.append(" AND n.isRead = :isRead");
            }
            if (filters.containsKey("notification_type")) {
                queryBuilder.append(" AND n.notificationType = :notificationType");
            }
            if (filters.containsKey("start_date")) {
                queryBuilder.append(" AND n.createdAt >= :startDate");
            }
            if (filters.containsKey("end_date")) {
                queryBuilder.append(" AND n.createdAt <= :endDate");
            }
        }

        queryBuilder.append(" ORDER BY n.createdAt DESC");

        TypedQuery<Notifications> query = em.createQuery(queryBuilder.toString(), Notifications.class);
        query.setParameter("userId", userId);

        if (filters != null) {
            if (filters.containsKey("is_read")) {
                query.setParameter("isRead", (Boolean) filters.get("is_read"));
            }
            if (filters.containsKey("notification_type")) {
                query.setParameter("notificationType", filters.get("notification_type"));
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (filters.containsKey("start_date")) {
                query.setParameter("startDate", LocalDate.parse((String) filters.get("start_date"), formatter).atStartOfDay());
            }
            if (filters.containsKey("end_date")) {
                query.setParameter("endDate", LocalDate.parse((String) filters.get("end_date"), formatter).atTime(23, 59, 59));
            }
        }

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        return query.getResultList();
    }

    public long countForUser(String userId, Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(n) FROM Notifications n WHERE n.userId = :userId");

        if (filters != null) {
            if (filters.containsKey("is_read")) {
                queryBuilder.append(" AND n.isRead = :isRead");
            }
            if (filters.containsKey("notification_type")) {
                queryBuilder.append(" AND n.notificationType = :notificationType");
            }
            if (filters.containsKey("start_date")) {
                queryBuilder.append(" AND n.createdAt >= :startDate");
            }
            if (filters.containsKey("end_date")) {
                queryBuilder.append(" AND n.createdAt <= :endDate");
            }
        }

        TypedQuery<Long> query = em.createQuery(queryBuilder.toString(), Long.class);
        query.setParameter("userId", userId);

        if (filters != null) {
            if (filters.containsKey("is_read")) {
                query.setParameter("isRead", (Boolean) filters.get("is_read"));
            }
            if (filters.containsKey("notification_type")) {
                query.setParameter("notificationType", filters.get("notification_type"));
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (filters.containsKey("start_date")) {
                query.setParameter("startDate", LocalDate.parse((String) filters.get("start_date"), formatter).atStartOfDay());
            }
            if (filters.containsKey("end_date")) {
                query.setParameter("endDate", LocalDate.parse((String) filters.get("end_date"), formatter).atTime(23, 59, 59));
            }
        }

        return query.getSingleResult();
    }

    public Notifications getById(String notificationId) {
        return em.find(Notifications.class, notificationId);
    }

    @Transactional
    public Notifications create(Notifications notification) {
        em.persist(notification);
        return notification;
    }

    @Transactional
    public Notifications markAsRead(String notificationId) {
        Notifications notification = getById(notificationId);
        if (notification == null) {
            return null;
        }
        notification.setRead(true);
        em.merge(notification);
        return notification;
    }

    @Transactional
    public void markAllAsRead(String userId) {
        em.createQuery("UPDATE Notifications n SET n.isRead = true WHERE n.userId = :userId AND n.isRead = false")
          .setParameter("userId", userId)
          .executeUpdate();
    }
}
