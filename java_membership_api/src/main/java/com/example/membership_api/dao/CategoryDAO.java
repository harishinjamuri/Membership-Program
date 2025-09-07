package com.example.membership_api.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.Category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class CategoryDAO {

    @PersistenceContext
    private EntityManager em;

    public List<Category> getAll(int page, int perPage, boolean onlyActive) {
        String queryString = "FROM Category c" + (onlyActive ? " WHERE c.isActive = true" : "");
        return em.createQuery(queryString, Category.class)
                 .setFirstResult((page - 1) * perPage)
                 .setMaxResults(perPage)
                 .getResultList();
    }

    public long getTotal(boolean onlyActive) {
        String queryString = "SELECT COUNT(c) FROM Category c" + (onlyActive ? " WHERE c.isActive = true" : "");
        return em.createQuery(queryString, Long.class).getSingleResult();
    }

    public Category getById(String id) {
        Category category = em.find(Category.class, id);
        return (category != null && category.isActive()) ? category : null;
    }

    public Category create(Category category) {
        em.persist(category);
        return category;
    }

    public Category update(Category existing, Category updates) {
        existing.setName(updates.getName());
        existing.setDescription(updates.getDescription());
        existing.setActive(updates.isActive());
        return existing;
    }

    public boolean softDelete(String id) {
        Category category = getById(id);
        if (category == null) return false;
        category.setActive(false);
        return true;
    }
}
