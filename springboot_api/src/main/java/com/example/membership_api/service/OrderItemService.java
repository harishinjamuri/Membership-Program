package com.example.membership_api.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.dao.OrderItemDAO;
import com.example.membership_api.model.OrderItem;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Autowired
    private OrderService orderService;

    public List<OrderItem> getAllForOrder(String orderId) {
        return orderItemDAO.getAllForOrder(orderId);
    }

    public Optional<OrderItem> getById(String orderItemId) {
        return Optional.ofNullable(orderItemDAO.getById(orderItemId));
    }

    public OrderItem create(String orderId, Map<String, Object> data) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(orderId);
        orderItem.setProductId( (String) data.get("product_id") );
        orderItem.setProductName( (String) data.get("product_name") );
        orderItem.setQuantity( ((Number) data.get("quantity")).intValue() );
        orderItem.setUnitPrice( ((Number) data.get("unit_price")).doubleValue() );
        orderItem.setTotalPrice( ((Number) data.get("total_price")).doubleValue() );
        if (data.containsKey("discount_value"))
            orderItem.setDiscountValue(((Number) data.get("discount_value")).doubleValue());

        if (data.containsKey("discount_applied"))
            orderItem.setDiscountApplied(((Boolean) data.get("discount_applied")).booleanValue());

        if (data.containsKey("discount_id") && data.get("discount_id") != null)
            orderItem.setDiscountId((String) data.get("discount_id"));

        if (data.containsKey("total_discount_value"))
            orderItem.setTotalDiscountValue(((Number) data.get("total_discount_value")).doubleValue());

        OrderItem saved = orderItemDAO.create(orderItem);
        orderService.recalculateOrder(orderId);
        return saved;
    }

    public Optional<OrderItem> update(String orderItemId, OrderItem updatedData) {
        OrderItem existing = orderItemDAO.getById(orderItemId);
        if (existing == null) {
            return Optional.empty();
        }
        if (updatedData.getProductId() != null) existing.setProductId(updatedData.getProductId());
        if (updatedData.getProductName() != null) existing.setProductName(updatedData.getProductName());
        if (updatedData.getQuantity() != null) existing.setQuantity(updatedData.getQuantity());
        if (updatedData.getUnitPrice() != null) existing.setUnitPrice(updatedData.getUnitPrice());
        if (updatedData.getTotalPrice() != null) existing.setTotalPrice(updatedData.getTotalPrice());
        if (updatedData.getDiscountApplied() != null) existing.setDiscountApplied(updatedData.getDiscountApplied());
        if (updatedData.getDiscountValue() != null) existing.setDiscountValue(updatedData.getDiscountValue());
        if (updatedData.getDiscountId() != null) existing.setDiscountId(updatedData.getDiscountId());
        if (updatedData.getTotalDiscountValue() != null) existing.setTotalDiscountValue(updatedData.getTotalDiscountValue());

        OrderItem saved = orderItemDAO.update(existing);
        orderService.recalculateOrder(existing.getOrderId());
        return Optional.of(saved);
    }

    public boolean delete(String orderItemId) {
        OrderItem oi = orderItemDAO.getById(orderItemId);
        if (oi == null) {
            return false;
        }
        boolean deleted = orderItemDAO.delete(orderItemId);
        if (deleted) {
            orderService.recalculateOrder(oi.getOrderId());
        }
        return deleted;
    }
}
