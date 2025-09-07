package com.example.membership_api.controller;

import java.util.List;
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
import com.example.membership_api.model.TierBenefits;
import com.example.membership_api.service.TierBenefitService;

@RestController
@RequestMapping("/benefits")
public class TierBenefitController {

    @Autowired
    private TierBenefitService service;

    // GET /benefits?active=true&page=1&perPage=10
    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "true") boolean active,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        List<TierBenefits> benefits = service.getAll(active, page, perPage);
        return ResponseEntity.ok(Map.of(
                "message", "Tier benefits fetched",
                "data", benefits
        ));
    }

    // GET /benefits/{benefitId}
    @GetMapping("/{benefitId}")
    public ResponseEntity<?> getOne(@PathVariable String benefitId) {
        TierBenefits benefit = service.getById(benefitId);
        if (benefit == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Benefit not found"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Benefit fetched",
                "data", benefit
        ));
    }

    // GET /benefits/tier/{tierId}
    @GetMapping("/tier/{tierId}")
    public ResponseEntity<?> getByTier(@PathVariable String tierId) {
        List<TierBenefits> benefits = service.getByTierId(tierId);
        if (benefits.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "No benefits found for the given tier"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Benefits by tier fetched",
                "data", benefits
        ));
    }

    // POST /benefits
    @PostMapping
    @AdminOnly
    public ResponseEntity<?> create(@RequestBody Map<String, Object>  benefit) {
        try {
            TierBenefits created = service.create(benefit);
            return ResponseEntity.status(201).body(Map.of(
                    "message", "Benefit created",
                    "data", created
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    // PUT /benefits/{benefitId}
    @PutMapping("/{benefitId}")
    @AdminOnly
    public ResponseEntity<?> update(@PathVariable String benefitId, @RequestBody Map<String, Object>  updates) {
        TierBenefits updated = service.update(benefitId, updates);
        if (updated == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Benefit not found"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Benefit updated",
                "data", updated
        ));
    }

    // DELETE /benefits/{benefitId}
    @DeleteMapping("/{benefitId}")
    @AdminOnly
    public ResponseEntity<?> delete(@PathVariable String benefitId) {
        boolean deleted = service.softDelete(benefitId);
        if (!deleted) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Benefit not found"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Benefit deleted"
        ));
    }
}
