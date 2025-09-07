package com.example.membership_api.controller;

import java.util.HashMap;
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

import com.example.membership_api.model.Product;
import com.example.membership_api.service.ProductService;
import com.example.membership_api.utils.ResponseUtil;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false, name = "category_id") String categoryId,
            @RequestParam(required = false) Boolean discounted,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Boolean exclusive
    ) {
        Map<String, Object> filters = new HashMap<>();
        if (categoryId != null) filters.put("category_id", categoryId);
        if (discounted != null) filters.put("discounted", discounted);
        if (product != null) filters.put("product", product);
        if (inStock != null) filters.put("in_stock", inStock);
        if (exclusive != null) filters.put("exclusive", exclusive);

        Map<String, Object> data = productService.getAll(page, perPage, filters);
        return ResponseUtil.success("Products retrieved", data);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getOne(@PathVariable String productId) {
        Product product = productService.getById(productId);
        if (product == null) {
            return ResponseUtil.error("Product not found", 404);
        }
        return ResponseUtil.success("Product found", product);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, Object> data) {
        Product created = productService.create(data);
        return ResponseUtil.success("Product created", created);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> update(@PathVariable String productId, @RequestBody Map<String, Object> data) {
        Product updated = productService.update(productId, data);
        if (updated == null) {
            return ResponseUtil.error("Product not found", 404);
        }
        return ResponseUtil.success("Product updated", updated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> delete(@PathVariable String productId) {
        boolean deleted = productService.delete(productId);
        if (!deleted) {
            return ResponseUtil.error("Product not found", 404);
        }
        return ResponseUtil.success("Product deleted successfully", null);
    }
}
