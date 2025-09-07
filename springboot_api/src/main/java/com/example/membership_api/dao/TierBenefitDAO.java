package com.example.membership_api.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.Subscription;
import com.example.membership_api.model.TierBenefits;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class TierBenefitDAO {

    @PersistenceContext
    private EntityManager em;

    public List<TierBenefits> getAll(boolean onlyActive, int page, int perPage) {
        String query = "FROM TierBenefits b" + (onlyActive ? " WHERE b.isActive = true" : "") + " ORDER BY b.sortOrder";
        return em.createQuery(query, TierBenefits.class)
                .setFirstResult((page - 1) * perPage)
                .setMaxResults(perPage)
                .getResultList();
    }

    public long getTotal(boolean onlyActive) {
        String query = "SELECT COUNT(b) FROM TierBenefits b" + (onlyActive ? " WHERE b.isActive = true" : "");
        return em.createQuery(query, Long.class).getSingleResult();
    }

    public TierBenefits getById(String id) {
        TierBenefits benefit = em.find(TierBenefits.class, id);
        return (benefit != null && benefit.isActive()) ? benefit : null;
    }

    public List<TierBenefits> getByTierId(String tierId) {
        String query = "FROM TierBenefits b WHERE b.tierId = :tierId AND b.isActive = true ORDER BY b.sortOrder";
        return em.createQuery(query, TierBenefits.class)
                .setParameter("tierId", tierId)
                .getResultList();
    }

    public TierBenefits create(TierBenefits  benefit) {
        em.persist(benefit);
        return benefit;
    }

    public TierBenefits update(TierBenefits existing, Map<String, Object>  updates) {
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
        TierBenefits benefit = getById(id);
        if (benefit == null) return false;
        benefit.setActive(false);
        return true;
    }
}
