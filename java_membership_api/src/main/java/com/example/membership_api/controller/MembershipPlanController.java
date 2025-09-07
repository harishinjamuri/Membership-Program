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
import com.example.membership_api.model.MembershipPlan;
import com.example.membership_api.service.MembershipPlanService;
import com.example.membership_api.utils.ResponseUtil;

@RestController
@RequestMapping("/plans")
public class MembershipPlanController {

    @Autowired
    private MembershipPlanService membershipPlanService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam Map<String, String> filters
    ) {
        return ResponseUtil.success("Membership plans retrieved",
                membershipPlanService.getAll(page, perPage, filters));
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getOne(@PathVariable String planId) {
        MembershipPlan plan = membershipPlanService.getById(planId);
        return ResponseUtil.success("Plan fetched", plan);
    }

    @PostMapping
    @AdminOnly
    public ResponseEntity<?> create(@RequestBody Map<String, Object> plan) {
        MembershipPlan created = membershipPlanService.create(plan);
        return ResponseUtil.success("Plan created", created);
    }

    @PutMapping("/{planId}")
    @AdminOnly
    public ResponseEntity<?> update(@PathVariable String planId, @RequestBody Map<String, Object> plan) {
        MembershipPlan updated = membershipPlanService.update(planId, plan);
        return ResponseUtil.success("Plan updated", updated);
    }

    @DeleteMapping("/{planId}")
    @AdminOnly
    public ResponseEntity<?> delete(@PathVariable String planId) {
        membershipPlanService.softDelete(planId);
        return ResponseUtil.success("Deleted successfully");
    }
}
