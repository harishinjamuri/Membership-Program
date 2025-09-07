package com.example.membership_api.service;

import com.example.membership_api.dao.CategoryDAO;
import com.example.membership_api.model.Category;
import com.example.membership_api.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO dao;

    public Map<String, Object> getAll(int page, int perPage, boolean onlyActive) {
        List<Category> items = dao.getAll(page, perPage, onlyActive);
        long total = dao.getTotal(onlyActive);
        return Map.of(
                "items", items,
                "total", total,
                "page", page,
                "per_page", perPage
        );
    }

    public Category getById(String id) {
        Category category = dao.getById(id);
        if (category == null) throw new NotFoundException("Category not found");
        return category;
    }

    public Category create(Category category) {
        return dao.create(category);
    }

    public Category update(String id, Category updates) {
        Category existing = getById(id);
        return dao.update(existing, updates);
    }

    public void softDelete(String id) {
        if (!dao.softDelete(id)) {
            throw new NotFoundException("Category not found");
        }
    }
}
