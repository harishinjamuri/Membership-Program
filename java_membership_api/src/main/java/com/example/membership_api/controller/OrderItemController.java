package com.example.membership_api.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.model.OrderItem;
import com.example.membership_api.service.OrderItemService;
import com.example.membership_api.utils.ResponseUtil;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getByOrder(@PathVariable String orderId) {
        List<OrderItem> items = orderItemService.getAllForOrder(orderId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{orderItemId}")
    public ResponseEntity<?> getOne(@PathVariable String orderItemId) {
        Optional<OrderItem> optionalItem = orderItemService.getById(orderItemId);
        if (optionalItem.isPresent()) {
            return ResponseUtil.success("Order item retrieved successfully", optionalItem.get());
        } else {
            return ResponseUtil.error("Order item not found", 404);
        }
    }


    @PostMapping("/{orderId}")
    public ResponseEntity<?> create(@PathVariable String orderId, @RequestBody Map<String, Object> data) {
        OrderItem created = orderItemService.create(orderId, data);
        return ResponseUtil.success("Order item created successfully", created);
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<?> update(@PathVariable String orderItemId, @RequestBody OrderItem data) {
        Optional<OrderItem> updated = orderItemService.update(orderItemId, data);
        if (updated.isPresent()) {
            return ResponseUtil.success("Order item updated successfully", updated);
        }
        else{
            return ResponseUtil.error("Order item not found", 404);
        }
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<?> delete(@PathVariable String orderItemId) {
        boolean deleted = orderItemService.delete(orderItemId);
        if (!deleted) {
            return ResponseEntity.status(404).body("Order item not found");
        }
        return ResponseEntity.ok(ResponseUtil.success("Order item deleted successfully"));
    }
}
