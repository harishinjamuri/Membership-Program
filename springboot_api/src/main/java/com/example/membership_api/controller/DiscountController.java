package com.example.membership_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.membership_api.model.Discount;
import com.example.membership_api.service.DiscountService;
import com.example.membership_api.utils.ResponseUtil;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping("")
    public Object getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam Map<String, Object> filters
    ) {
        try {
            Map<String, Object> result = discountService.getAll(page, perPage, filters);
            return ResponseUtil.success("Discounts fetched successfully", result);
        } catch (Exception e) {
            return ResponseUtil.error("Error fetching discounts: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Object getById(@PathVariable String id) {
        Discount discount = discountService.getById(id);
        if (discount == null) {
            return ResponseUtil.error("Discount not found", 404);
        }
        return ResponseUtil.success("Discount fetched successfully", discount);
    }

    @GetMapping("/code/{code}")
    public Object getByCode(@PathVariable String code) {
        Discount discount = discountService.getByCode(code);
        if (discount == null) {
            return ResponseUtil.error("Discount not found", 404);
        }
        return ResponseUtil.success("Discount fetched successfully", discount);
    }

    @PostMapping("")
    @AdminOnly
    public Object create(@RequestBody Map<String, Object> data) {
        try {
            Object[] result = discountService.create(data);
            if ((boolean) result[1]) {
                return ResponseUtil.success("Discount created successfully", result[0]);
            } else {
                return ResponseUtil.error((String) result[0]);
            }
        } catch (Exception e) {
            return ResponseUtil.error("Error creating discount: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @AdminOnly
    public Object update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        try {
            Discount updated = discountService.update(id, data);
            return ResponseUtil.success("Discount updated successfully", updated);
        } catch (Exception e) {
            return ResponseUtil.error("Error updating discount: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public Object delete(@PathVariable String id) {
        try {
            boolean deleted = discountService.softDelete(id);
            if (deleted) {
                return ResponseUtil.success("Discount deleted successfully");
            } else {
                return ResponseUtil.error("Discount not found", 404);
            }
        } catch (Exception e) {
            return ResponseUtil.error("Error deleting discount: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public Object validateDiscount(@RequestBody Map<String, Object> payload) {
        try {
            Object[] result = discountService.validateDiscount(payload);
            boolean isValid = (boolean) result[0];
            String message = (String) result[1];
            if (isValid) {
                return ResponseUtil.success(message);
            } else {
                return ResponseUtil.error(message);
            }
        } catch (Exception e) {
            return ResponseUtil.error("Error validating discount: " + e.getMessage());
        }
    }
}
