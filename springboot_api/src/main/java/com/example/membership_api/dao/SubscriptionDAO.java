package com.example.membership_api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.Subscription;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class SubscriptionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch paginated subscriptions with optional filters.
     */
    public Map<String, Object> getAll(int page, int perPage, Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT s FROM Subscription s WHERE 1=1");
        StringBuilder countBuilder = new StringBuilder("SELECT COUNT(s) FROM Subscription s WHERE 1=1");

        Map<String, Object> params = new HashMap<>();

        if (filters != null) {
            if (filters.containsKey("user_id")) {
                queryBuilder.append(" AND s.userId = :userId");
                countBuilder.append(" AND s.userId = :userId");
                params.put("userId", filters.get("user_id"));
            }

            if (filters.containsKey("is_active")) {
                queryBuilder.append(" AND s.isActive = :isActive");
                countBuilder.append(" AND s.isActive = :isActive");
                params.put("isActive", filters.get("is_active"));
            }
        }

        queryBuilder.append(" ORDER BY s.startDate DESC");

        TypedQuery<Subscription> query = entityManager.createQuery(queryBuilder.toString(), Subscription.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countBuilder.toString(), Long.class);

        params.forEach((key, value) -> {
            query.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        List<Subscription> items = query.getResultList();
        Long total = countQuery.getSingleResult();

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", total);
        return result;
    }

    /**
     * Fetch subscription by ID (only active).
     */
    public Subscription getById(String subscriptionId) {
        String jpql = "SELECT s FROM Subscription s WHERE s.id = :id AND s.isActive = true";
        List<Subscription> results = entityManager.createQuery(jpql, Subscription.class)
                .setParameter("id", subscriptionId)
                .setMaxResults(1)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Create new subscription.
     */
    public Subscription create(Subscription subscription) {
        entityManager.persist(subscription);
        return subscription;
    }

    /**
     * Get most recent active subscription by user ID.
     */
    public Subscription getActiveByUser(String userId) {
        String jpql = "SELECT s FROM Subscription s WHERE s.userId = :userId AND s.isActive = true ORDER BY s.endDate DESC";
        List<Subscription> results = entityManager.createQuery(jpql, Subscription.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Save or update a subscription.
     */
    public Subscription save(Subscription subscription) {
        return entityManager.merge(subscription);
    }

    /**
     * Update subscription by ID.
     */
    public Subscription update(String subscriptionId, Map<String, Object> data) {
        Subscription subscription = getById(subscriptionId);
        if (subscription == null) {
            return null;
        }

        data.forEach((key, value) -> {
            try {
                var field = Subscription.class.getDeclaredField(key);
                field.setAccessible(true);
                
                // You may want to handle primitive types explicitly here if needed
                field.set(subscription, value);
            } catch (NoSuchFieldException e) {
                // Optionally log unknown field key
                System.out.printf("Warning: No such field '%s' in Subscription%n", key);
            } catch (IllegalAccessException e) {
                System.out.printf("Warning: Cannot access field '%s'%n", key);
            } catch (IllegalArgumentException e) {
                System.out.printf("Warning: Invalid type for field '%s' with value '%s'%n", key, value);
            }
        });

        return save(subscription);
    }


    /**
     * Soft delete subscription by setting isActive = false.
     */
    public boolean softDelete(String subscriptionId) {
        Subscription subscription = getById(subscriptionId);
        if (subscription == null) {
            return false;
        }

        subscription.setIsActive(false);
        save(subscription);
        return true;
    }
}
