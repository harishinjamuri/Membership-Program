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
import com.example.membership_api.model.MembershipTier;
import com.example.membership_api.service.MembershipTierService;
import com.example.membership_api.utils.ResponseUtil;

@RestController
@RequestMapping("/tiers")
public class MembershipTierController {

    @Autowired
    private MembershipTierService membershipTierService;

    @GetMapping("")
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Integer tierLevel) {

        // Build filter map
        Map<String, Object> filters = new java.util.HashMap<>();
        if (active != null) filters.put("isActive", active);
            
        Map<String, Object> data = membershipTierService.getAll(page, perPage, filters);
        return ResponseUtil.success(null, data);
    }

    @GetMapping("/{tierId}")
    public ResponseEntity<?> getOne(@PathVariable String tierId) {
        MembershipTier tier = membershipTierService.getById(tierId);
        if (tier == null) {
            return ResponseUtil.error("Tier not found", 404);
        }
        return ResponseUtil.success( null,tier);
    }

    @PostMapping("")
    @AdminOnly
    public ResponseEntity<?> create(@RequestBody Map<String, Object> membershipTier) {
        MembershipTier created = membershipTierService.create(membershipTier);
        return ResponseUtil.success("Tier created", created);
    }

    @PutMapping("/{tierId}")
    @AdminOnly
    public ResponseEntity<?> update(@PathVariable String tierId, @RequestBody Map<String, Object> membershipTier) {
        MembershipTier updated = membershipTierService.update(tierId, membershipTier);
        if (updated == null) {
            return ResponseUtil.error("Tier not found", 404);
        }
        return ResponseUtil.success("Tier updated", updated);
    }

    @DeleteMapping("/{tierId}")
    @AdminOnly
    public ResponseEntity<?> delete(@PathVariable String tierId) {
        boolean deleted = membershipTierService.softDelete(tierId);
        if (!deleted) {
            return ResponseUtil.error("Tier not found", 404);
        }
        return ResponseUtil.success("Deleted successfully");
    }
}
