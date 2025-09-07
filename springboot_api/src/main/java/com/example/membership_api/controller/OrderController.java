package com.example.membership_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.model.Order;
import com.example.membership_api.service.OrderService;
import com.example.membership_api.utils.JwtUtil;
import com.example.membership_api.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/me")
    public ResponseEntity<Map<String,Object>> getMyOrders(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="10") int perPage,
            HttpServletRequest request) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        var items = orderService.getAll(page, perPage, null, userId);
        var total = orderService.countAll(null, userId);
        return ResponseEntity.ok(Map.of("items", items, "total", total, "page", page, "perPage", perPage));
    }

    @GetMapping("")
    public ResponseEntity<Map<String,Object>> getAllOrders(
            @RequestParam(defaultValue="1") int page,
            @RequestParam(defaultValue="10") int perPage,
            @RequestParam(required=false) String status,
            @RequestParam(required=false, name="user_id") String userId) {
        var items = orderService.getAll(page, perPage, status, userId);
        var total = orderService.countAll(status, userId);
        return ResponseEntity.ok(Map.of("items", items, "total", total, "page", page, "perPage", perPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOne(@PathVariable String id) {
        return orderService.getById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public Object create(HttpServletRequest request,@RequestBody Map<String, Object>  data) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        data.put("user_id",userId);
        return ResponseEntity.ok(ResponseUtil.success("Order Created Succefully",orderService.create(data)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable String id, @RequestBody Order order) {
        return orderService.update(id, order)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<?> confirm(@PathVariable String id, HttpServletRequest request) {

        String userId = JwtUtil.extractUserIdFromRequest(request);
        try{
            return orderService.confirm(userId, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException ex) {
            return ResponseUtil.error(ex.getMessage(), 400);
        }
    }
}
