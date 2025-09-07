package com.example.membership_api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.dao.ProductDAO;
import com.example.membership_api.model.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public Map<String, Object> getAll(int page, int perPage, Map<String, Object> filters) {
        List<Product> products = productDAO.getAll(page, perPage, filters);

        long total = productDAO.getTotalCount(filters);

        Map<String, Object> response = new HashMap<>();
        response.put("items", products);
        response.put("total", total);
        return response;
    }

    public Product getById(String productId) {
        return productDAO.getById(productId);
    }

    public Product create(Map<String, Object> data) {
        Product product = new Product();
        applyUpdates(product, data);
        return productDAO.create(product);
    }

    public Product update(String productId, Map<String, Object> data) {
        Product existing = productDAO.getById(productId);
        if (existing == null) return null;
        applyUpdates(existing, data);
        productDAO.commit(existing);
        return existing;
    }

    public void commit(Product product){
        productDAO.commit(product);
    }

    public boolean delete(String productId) {
        return productDAO.softDelete(productId);
    }

    private void applyUpdates(Product product, Map<String, Object> data) {
        if (data.containsKey("name")) product.setName((String) data.get("name"));
        if (data.containsKey("description")) product.setDescription((String) data.get("description"));
        if (data.containsKey("category_id")) product.setCategoryId((String) data.get("category_id"));
        if (data.containsKey("stock_quantity")) product.setStockQuantity((Integer) data.get("stock_quantity"));
        if (data.containsKey("discount_id")) product.setDiscountId((String) data.get("discount_id"));
        if (data.containsKey("price")) product.setPrice(Double.valueOf(data.get("price").toString()));
        if (data.containsKey("is_membership_exclusive"))
            product.setIsMembershipExclusive((Boolean) data.get("is_membership_exclusive"));
        if (data.containsKey("min_tier_required"))
            product.setMinTierRequired((Integer) data.get("min_tier_required"));
    }
}
