package com.example.membership_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.annotations.AdminOnly;
import com.example.membership_api.model.Category;
import com.example.membership_api.service.CategoryService;
import com.example.membership_api.utils.ResponseUtil;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(name = "active", required = false, defaultValue = "true") boolean onlyActive
    ) {
        Map<String, Object> data = service.getAll(page, perPage, onlyActive);
        return ResponseUtil.success("Categories retrieved", data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        Category category = service.getById(id);
        return ResponseUtil.success("Category found", category);
    }

    @PostMapping
    @AdminOnly
    public ResponseEntity<?> create(@RequestBody Category category) {
        Category created = service.create(category);
        return ResponseUtil.success("Category created", created);
    }

    @PutMapping("/{id}")
    @AdminOnly
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Category updates) {
        Category updated = service.update(id, updates);
        return ResponseUtil.success("Category updated", updated);
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public ResponseEntity<?> delete(@PathVariable String id) {
        service.softDelete(id);
        return ResponseUtil.success("Category deleted successfully");
    }
}
