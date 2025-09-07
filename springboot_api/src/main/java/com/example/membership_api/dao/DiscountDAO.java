package com.example.membership_api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.membership_api.exception.NotFoundException;
import com.example.membership_api.model.Discount;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class DiscountDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Object> getAll(int page, int perPage, Map<String, Object> filters) {
        StringBuilder jpql = new StringBuilder("SELECT d FROM Discount d WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (filters.containsKey("active")) {
            jpql.append(" AND d.isActive = :active");
            params.put("active", filters.get("active"));
        }

        if (filters.containsKey("discount_type")) {
            jpql.append(" AND d.discountType = :discountType");
            params.put("discountType", filters.get("discount_type"));
        }

        jpql.append(" ORDER BY d.createdAt DESC");

        TypedQuery<Discount> query = entityManager.createQuery(jpql.toString(), Discount.class);
        params.forEach(query::setParameter);

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        List<Discount> items = query.getResultList();

        // Count query
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(d) FROM Discount d WHERE 1=1");
        if (filters.containsKey("active")) {
            countJpql.append(" AND d.isActive = :active");
        }
        if (filters.containsKey("discount_type")) {
            countJpql.append(" AND d.discountType = :discountType");
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);
        params.forEach(countQuery::setParameter);

        long total = countQuery.getSingleResult();

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", total);
        return result;
    }
     
    public Discount getById(String discountId) {
        String jpql = "SELECT s FROM Discount s WHERE s.id = :id AND s.isActive = true";
        List<Discount> results = entityManager.createQuery(jpql, Discount.class)
                .setParameter("id", discountId)
                .setMaxResults(1)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }


    public Discount getByCode(String code) {
        String jpql = "SELECT d FROM Discount d WHERE d.code = :code AND d.isActive = true";
        TypedQuery<Discount> query = entityManager.createQuery(jpql, Discount.class);
        query.setParameter("code", code);
        return query.getResultStream().findFirst().orElse(null);
    }

    public Discount create(Discount discount) {
        entityManager.persist(discount);
        return discount;
    }

    public Discount update(Discount discount) {
        return entityManager.merge(discount);
    }

    public boolean softDelete(String id) {
        Discount discount = getById(id);
        if (discount == null) {
            throw new NotFoundException("Discount not found");
        }
        discount.setActive(false);
        entityManager.merge(discount);
        return true;
    }
}
