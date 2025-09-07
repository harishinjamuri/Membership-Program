package com.example.membership_api.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class ProductDAO {

    @PersistenceContext
    private EntityManager em;

    public List<Product> getAll(int page, int perPage, Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("FROM Product p WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        if (filters != null) {
            if (filters.containsKey("category_id")) {
                queryBuilder.append("AND p.categoryId = :categoryId ");
                params.put("categoryId", filters.get("category_id"));
            }
            if (filters.containsKey("discounted")) {
                if (Boolean.TRUE.equals(filters.get("discounted"))) {
                    queryBuilder.append("AND p.discountId IS NOT NULL ");
                } else {
                    queryBuilder.append("AND p.discountId IS NULL ");
                }
            }
            if (filters.containsKey("in_stock")) {
                if (Boolean.TRUE.equals(filters.get("in_stock"))) {
                    queryBuilder.append("AND p.stockQuantity > 0 ");
                } else {
                    queryBuilder.append("AND p.stockQuantity <= 0 ");
                }
            }
            if (filters.containsKey("exclusive")) {
                queryBuilder.append("AND p.isMembershipExclusive = :exclusive ");
                params.put("exclusive", filters.get("exclusive"));
            }
            if (filters.containsKey("product")) {
                queryBuilder.append("AND LOWER(p.name) LIKE :product ");
                params.put("product", "%" + filters.get("product").toString().toLowerCase() + "%");
            }
        }

        TypedQuery<Product> query = em.createQuery(queryBuilder.toString(), Product.class);

        params.forEach(query::setParameter);

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        return query.getResultList();
    }

    public long getTotalCount(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        if (filters != null) {
            if (filters.containsKey("category_id")) {
                queryBuilder.append("AND p.categoryId = :categoryId ");
                params.put("categoryId", filters.get("category_id"));
            }
            if (filters.containsKey("discounted")) {
                if (Boolean.TRUE.equals(filters.get("discounted"))) {
                    queryBuilder.append("AND p.discountId IS NOT NULL ");
                } else {
                    queryBuilder.append("AND p.discountId IS NULL ");
                }
            }
            if (filters.containsKey("in_stock")) {
                if (Boolean.TRUE.equals(filters.get("in_stock"))) {
                    queryBuilder.append("AND p.stockQuantity > 0 ");
                } else {
                    queryBuilder.append("AND p.stockQuantity <= 0 ");
                }
            }
            if (filters.containsKey("exclusive")) {
                queryBuilder.append("AND p.isMembershipExclusive = :exclusive ");
                params.put("exclusive", filters.get("exclusive"));
            }
            if (filters.containsKey("product")) {
                queryBuilder.append("AND LOWER(p.name) LIKE :product ");
                params.put("product", "%" + filters.get("product").toString().toLowerCase() + "%");
            }
        }

        TypedQuery<Long> query = em.createQuery(queryBuilder.toString(), Long.class);
        params.forEach(query::setParameter);

        return query.getSingleResult();
    }

    public Product getById(String productId) {
        return em.find(Product.class, productId);
    }

    public Product create(Product product) {
        em.persist(product);
        return product;
    }

    public Product update(Product existing, Map<String, Object> updates) {
        if (updates.containsKey("name")) existing.setName((String) updates.get("name"));
        if (updates.containsKey("description")) existing.setDescription((String) updates.get("description"));
        if (updates.containsKey("category_id")) existing.setCategoryId((String) updates.get("category_id"));
        if (updates.containsKey("stock_quantity")) existing.setStockQuantity((Integer) updates.get("stock_quantity"));
        if (updates.containsKey("discount_id")) existing.setDiscountId((String) updates.get("discount_id"));
        if (updates.containsKey("price")) existing.setPrice(Double.valueOf(updates.get("price").toString()));
        if (updates.containsKey("is_membership_exclusive"))
            existing.setIsMembershipExclusive((Boolean) updates.get("is_membership_exclusive"));
        if (updates.containsKey("min_tier_required"))
            existing.setMinTierRequired((Integer) updates.get("min_tier_required"));
        return existing;
    }

    public void commit(Product product) {
        em.merge(product);
    }

    public boolean softDelete(String productId) {
        Product product = getById(productId);
        if (product == null) return false;
        em.remove(product);
        return true;
    }
}
