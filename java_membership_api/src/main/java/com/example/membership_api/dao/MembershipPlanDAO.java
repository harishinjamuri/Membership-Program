package com.example.membership_api.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.membership_api.constants.PlanType;
import com.example.membership_api.model.MembershipPlan;
import com.example.membership_api.model.Subscription;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class MembershipPlanDAO {

    @PersistenceContext
    private EntityManager em;

    public MembershipPlan create(Map<String, Object> plan) {
        MembershipPlan memPlan = new MembershipPlan();
        memPlan.setName((String) plan.get("name"));
        memPlan.setDescription((String) plan.get("description"));
        memPlan.setPrice(((Number) plan.get("price")).doubleValue());
        String planTypeStr = (String) plan.get("plan_type");
        memPlan.setPlanType(PlanType.valueOf(planTypeStr.toUpperCase()));
        em.persist(memPlan);
        return memPlan;
    }

    public MembershipPlan getById(String id) {
        return em.find(MembershipPlan.class, id);
    }

    public List<MembershipPlan> getAll(Boolean isActive, int page, int perPage) {
        String baseQuery = "FROM MembershipPlan";
        if (isActive != null) {
            baseQuery += " WHERE isActive = :isActive";
        }

        var query = em.createQuery(baseQuery, MembershipPlan.class);

        if (isActive != null) {
            query.setParameter("isActive", isActive);
        }

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        return query.getResultList();
    }

    public long getTotal(Boolean isActive) {
        String baseQuery = "SELECT COUNT(p) FROM MembershipPlan p";
        if (isActive != null) {
            baseQuery += " WHERE p.isActive = :isActive";
        }

        var query = em.createQuery(baseQuery, Long.class);

        if (isActive != null) {
            query.setParameter("isActive", isActive);
        }

        return query.getSingleResult();
    }

    public MembershipPlan update(MembershipPlan existing, Map<String, Object> updates) {
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
        MembershipPlan plan = getById(id);
        if (plan == null) return false;

        plan.setActive(false);
        return true;
    }
}
