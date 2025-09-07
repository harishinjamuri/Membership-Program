package com.example.membership_api.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.DiscountUsage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class DiscountUsageDAO {

    @PersistenceContext
    private EntityManager em;

    public List<DiscountUsage> getAll(int page, int perPage, String userId, String discountId) {
        StringBuilder queryString = new StringBuilder("FROM DiscountUsage du WHERE 1=1 ");

        if (userId != null) {
            queryString.append("AND du.userId = :userId ");
        }
        if (discountId != null) {
            queryString.append("AND du.discountId = :discountId ");
        }

        var query = em.createQuery(queryString.toString(), DiscountUsage.class);

        if (userId != null) {
            query.setParameter("userId", userId);
        }
        if (discountId != null) {
            query.setParameter("discountId", discountId);
        }

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        return query.getResultList();
    }

    public long getTotal(String userId, String discountId) {
        StringBuilder queryString = new StringBuilder("SELECT COUNT(du) FROM DiscountUsage du WHERE 1=1 ");

        if (userId != null) {
            queryString.append("AND du.userId = :userId ");
        }
        if (discountId != null) {
            queryString.append("AND du.discountId = :discountId ");
        }

        var query = em.createQuery(queryString.toString(), Long.class);

        if (userId != null) {
            query.setParameter("userId", userId);
        }
        if (discountId != null) {
            query.setParameter("discountId", discountId);
        }

        return query.getSingleResult();
    }

    public DiscountUsage getById(String id) {
        return em.find(DiscountUsage.class, id);
    }

    public List<DiscountUsage> getByUser(String userId, int page, int perPage) {
        TypedQuery<DiscountUsage> query = em.createQuery(
                "FROM DiscountUsage du WHERE du.userId = :userId", DiscountUsage.class);
        query.setParameter("userId", userId);
        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);
        return query.getResultList();
    }

    public long getUserUsageCount(String userId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(du) FROM DiscountUsage du WHERE du.userId = :userId", Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public DiscountUsage create(DiscountUsage usage) {
        em.persist(usage);
        return usage;
    }

    public DiscountUsage update(DiscountUsage existing, DiscountUsage updates) {
        existing.setDiscountId(updates.getDiscountId());
        existing.setUserId(updates.getUserId());
        existing.setAppliedOn(updates.getAppliedOn());
        existing.setEntity(updates.getEntity());
        existing.setDiscountAmount(updates.getDiscountAmount());
        existing.setUsedAt(updates.getUsedAt());
        return existing;
    }

    public boolean softDelete(String id) {
        DiscountUsage usage = getById(id);
        if (usage == null) return false;
        // You might want to add an "active" flag on DiscountUsage for soft delete
        // If not, this method can simply return false or throw exception.
        return false; // Or implement soft delete logic here
    }
}
