package com.example.membership_api.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.membership_api.constants.TierType;
import com.example.membership_api.model.MembershipTier;
import com.example.membership_api.model.Subscription;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class MembershipTierDAO {

    @PersistenceContext
    private EntityManager em;

    public List<MembershipTier> getAll(int page, int perPage, Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("FROM MembershipTier t WHERE 1=1");

        if (filters.containsKey("is_active")) {
            queryBuilder.append(" AND t.isActive = :isActive");
        }
     

        var query = em.createQuery(queryBuilder.toString(), MembershipTier.class);

        if (filters.containsKey("is_active")) {
            Object value = filters.get("is_active");
            if (value instanceof Boolean) {
                query.setParameter("isActive", value);
            } else if (value instanceof String string) {
                query.setParameter("isActive", Boolean.valueOf(string));
            }
        }

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        return query.getResultList();
    }

    public long getTotal(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(t) FROM MembershipTier t WHERE 1=1");

        if (filters.containsKey("is_active")) {
            queryBuilder.append(" AND t.isActive = :isActive");
        }

        var query = em.createQuery(queryBuilder.toString(), Long.class);

        if (filters.containsKey("is_active")) {
            Object value = filters.get("is_active");
            if (value instanceof Boolean) {
                query.setParameter("isActive", value);
            } else if (value instanceof String string) {
                query.setParameter("isActive", Boolean.valueOf(string));
            }
        }
        return query.getSingleResult();
    }

      public MembershipTier getEligibleTier(Map<String, Object> data) {
        StringBuilder queryBuilder = new StringBuilder("FROM MembershipTier t WHERE 1=1");

        if (data.containsKey("monthly_orders")) {
            queryBuilder.append(" AND t.minMonthlyOrders <= :monthly_orders");
        }
        if (data.containsKey("monthly_spend")) {
            queryBuilder.append(" AND t.minMonthlySpend <= :monthly_spend");
        }
        if (data.containsKey("total_orders")) {
            queryBuilder.append(" AND t.minTotalOrders <= :total_orders");
        }
        if (data.containsKey("total_spend")) {
            queryBuilder.append(" AND t.minTotalSpend <= :total_spend");
        }

        queryBuilder.append(" ORDER BY t.tierLevel DESC");

        var query = em.createQuery(queryBuilder.toString(), MembershipTier.class);

        data.forEach(query::setParameter);

        return query.setMaxResults(1).getResultStream().findFirst().orElse(null);
    }
    
    public MembershipTier getById(String id) {
        return em.find(MembershipTier.class, id);
    }

    public List<MembershipTier> getActiveByTier(int tierLevel) {
        StringBuilder queryBuilder = new StringBuilder("FROM MembershipTier t WHERE 1=1");
        queryBuilder.append(" AND t.isActive = true");
        queryBuilder.append(" AND t.tierLevel = :tierLevel");

        var query = em.createQuery(queryBuilder.toString(), MembershipTier.class);
        query.setParameter("tierLevel", tierLevel);

        return query.getResultList();
    }

    public MembershipTier create(Map<String, Object> tier) {
        MembershipTier memTier = new MembershipTier();
        memTier.setName((String) tier.get("name"));
        memTier.setDescription((String) tier.get("description"));
        memTier.setMinMonthlyOrders( ((Number) tier.get("min_monthly_orders")).intValue() );
        memTier.setMinMonthlySpend( ((Number) tier.get("min_monthly_spend")).doubleValue() );
        memTier.setMinTotalOrders(((Number) tier.get("min_total_orders")).intValue());
        memTier.setMinTotalSpend(((Number) tier.get("min_total_spend")).doubleValue());
        
        String planTypeStr = (String) tier.get("tier");
        memTier.setTier(TierType.valueOf(planTypeStr.toUpperCase()));
        em.persist(memTier);
        return memTier;
    }

    public MembershipTier update(MembershipTier existing, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            try {
                var field = Subscription.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(existing, value);
                System.out.printf("field: %s key: %s Value: %s", field, key, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Ignore invalid fields
            }
        });

        return em.merge(existing);
    }

    public boolean softDelete(String id) {
        MembershipTier tier = em.find(MembershipTier.class, id);
        if (tier == null) return false;
        tier.setActive(false);
        return true;
    }

}
