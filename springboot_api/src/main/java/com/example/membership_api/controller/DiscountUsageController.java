package com.example.membership_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.annotations.AdminOnly;
import com.example.membership_api.model.DiscountUsage;
import com.example.membership_api.service.DiscountUsageService;
import com.example.membership_api.utils.JwtUtil;
import com.example.membership_api.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/discount-usages")
public class DiscountUsageController {

    @Autowired
    private DiscountUsageService service;

    @GetMapping
    @AdminOnly
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String discountId
    ) {
        Map<String, Object> data = service.getAll(page, perPage, userId, discountId);
        return ResponseUtil.success("Discount usages retrieved", data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        DiscountUsage usage = service.getById(id);
        if (usage == null) {
            return ResponseUtil.error("Discount usage not found", 404);
        }
        return ResponseUtil.success("Discount usage found", usage);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DiscountUsage discountUsage) {
        DiscountUsage created = service.create(
                discountUsage.getDiscountId(),
                discountUsage.getUserId(),
                discountUsage.getAppliedOn(),
                discountUsage.getEntity(),
                discountUsage.getDiscountAmount()
        );
        return ResponseUtil.success("Discount usage created", created);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyUsage(HttpServletRequest request, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
            ) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        List<DiscountUsage> usage = service.getByUser(page, perPage, userId);
        if (usage == null) {
            return ResponseUtil.error("Discount usage not found", 404);
        }
        return ResponseUtil.success("Discount usage found", usage);
    }
}
