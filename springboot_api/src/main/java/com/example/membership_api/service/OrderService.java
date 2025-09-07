package com.example.membership_api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.constants.BenefitType;
import com.example.membership_api.constants.EntityType;
import com.example.membership_api.constants.NotificationType;
import com.example.membership_api.constants.OrderStatus;
import com.example.membership_api.dao.OrderDAO;
import com.example.membership_api.dao.OrderItemDAO;
import com.example.membership_api.model.Notifications;
import com.example.membership_api.model.Order;
import com.example.membership_api.model.OrderItem;
import com.example.membership_api.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private DiscountUsageService discountUsageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private OrderItemDAO orderItemDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private UserTierMetricsService userTierMetricsService;
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private TierBenefitService tierBenefitService;
    @Autowired
    private MembershipTierService membershipTierService;

    public List<Order> getAll(int page, int perPage, String status, String userId) {
        return orderDAO.getAll(page, perPage, status, userId);
    }

    public long countAll(String status, String userId) {
        return orderDAO.count(status, userId);
    }

    public Optional<Order> getById(String id) {
        return orderDAO.getById(id);
    }

    public long getTotalOrders(String userId){
        return orderDAO.getTotalOrders(userId);
    }

    public Double getTotalSpend(String userId){
        return orderDAO.getTotalSpend(userId);
    }

    public Order create(Map<String, Object>  data) {
        Order order = new Order();
        order.setUserId((String) data.get("user_id"));
        order.setDeliveryAddress((String) data.get("delivery_address") );
        return orderDAO.create(order);
    }

    public Optional<Order> update(String id, Order data) {
        Optional<Order> optionalOrder = orderDAO.getById(id);
    
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setDeliveryAddress(data.getDeliveryAddress());
            order.setItems(data.getItems());
            order.setDiscounts(data.getDiscounts());
            order.setStatus(data.getStatus());
            order.setSubtotal(data.getSubtotal());
            order.setTotalAmount(data.getTotalAmount());
            order.setTotalSavings(data.getTotalSavings());
            order.setDeliveryFee(data.getDeliveryFee());
            order.setDeliveryFeeApplied(data.isDeliveryFeeApplied());

            return orderDAO.update(order); // returns Optional<Order>
        }

        return Optional.empty(); // in case not found

    }

    public Optional<Order> confirm(String userId, String orderId) {
        var userOpt = userService.getById(userId);
        if (userOpt.isEmpty()) throw new RuntimeException("User not found");

        Optional<Order> orderOpt = orderDAO.getById(orderId);
        if (orderOpt.isEmpty()) throw new RuntimeException("Order not found");

        Order order = orderOpt.get();
        if ( order.getStatus() != OrderStatus.PENDING ){
            throw new RuntimeException(String.format("Cannot confirm order with status: %s.", order.getStatus()));
        }
        
        order.setStatus(OrderStatus.CONFIRMED);

        if ( order.getOrderItemCount() <1 ){
            throw new RuntimeException("Cannot Confirm Order with out Any items");
        }

        List<String> orderIds = JsonUtils.fromJsonType(order.getItems(), new TypeReference<List<String>>() {});

        List<OrderItem> items = orderItemDAO.getByIds(orderIds);
        
        double totalAmount = items.stream()
            .mapToDouble(item -> 
                ((Number) item.getTotalPrice()).doubleValue()
                - ((Number) item.getDiscountValue()).doubleValue()
            )
            .sum();

        double deliveryCharges = totalAmount * 0.1;
        order.setDeliveryFee(deliveryCharges);
        order.setDeliveryFeeApplied(true);

        Set<String> discountIds = new HashSet<>();

        for (OrderItem item : items) {
            String productId = (String) item.getProductId();
            int quantity = ((Number) item.getQuantity()).intValue();
            String discountId = (String) item.getDiscountId();

            var product = productService.getById(productId);
            if (product != null) {
                if (product.isAvailable() && product.getStockQuantity() >= quantity) {
                    product.setStockQuantity(product.getStockQuantity() - quantity);
                    productService.commit(product);
                }
            }

            if (discountId != null && !discountId.isEmpty()) {
                discountIds.add(discountId);
                double discountValue = ((Number) item.getTotalDiscountValue()).doubleValue();
                discountUsageService.create(discountId, userId, order.getId(), EntityType.ORDER_ITEM, discountValue);
            }
        }

        discountIds.forEach(discountService::incrementUsageCount);

        // Send notification
        Notifications notification = new Notifications();
        notification.setUserId(userId);
        notification.setNotificationType(NotificationType.ORDER_UPDATE);
        notification.setTitle("Order Confirmed");
        notification.setMessage("Your order #" + order.getId() + " has been confirmed!");
        notification.setDetails(Map.of("order_id", order.getId(), "status", OrderStatus.CONFIRMED.getValue()).toString() );

        notificationService.create(notification);
       
        userTierMetricsService.createOrUpdate(userId, 1,totalAmount);

        // Subscription logic
        var subscription = subscriptionService.getActiveSubscriptionByUser(userId);
        subscription.setTotalSpent(subscription.getTotalSpent() + totalAmount);

        var benefits = tierBenefitService.getByTierId(subscription.getTierId());
        for (var benefit : benefits) {
            if (benefit.getBenefitType() == BenefitType.FREE_SHIPPING) {
                order.setTotalSavings(order.getTotalSavings() + deliveryCharges);
                order.setDeliveryFeeApplied(false);
            } else if (benefit.getBenefitType() == BenefitType.DISCOUNT) {
                Map<String, Object> value = JsonUtils.parseJsonToMap(benefit.getBenefitValue());
                double percent = ((Number) value.get("percentage")).doubleValue();
                String discountId = (String) value.get("discount_id");

                double discountAmount = Math.round(order.getTotalAmount() * (percent / 100.0) * 100.0) / 100.0;
                order.setTotalSavings(order.getTotalSavings() + discountAmount);

                discountUsageService.create(discountId, userId, order.getId(), EntityType.ORDER, discountAmount);
                discountService.incrementUsageCount(discountId);
                discountIds.add(discountId);
            }
        }

        order.setDiscounts(JsonUtils.toJson(discountIds));
        subscription.setTotalSaved(subscription.getTotalSaved() + order.getTotalSavings());
        subscriptionService.save(subscription);

        var eligibleTier = membershipTierService.getEligibleTier(userId);
        System.out.printf("eligibleTier: %s%n", eligibleTier);
        if (subscription.getCurrentTier() < eligibleTier.getTierLevel()) {
                subscriptionService.update(subscription.getId(), 
               Map.of(
                    "tierId", eligibleTier.getId(),
                    "currentTier", eligibleTier.getTierLevel()
                )
            );

            notification = new Notifications();
            notification.setUserId(userId);
            notification.setNotificationType(NotificationType.TIER_UPGRADE);
            notification.setTitle("Tier Upgraded");
            notification.setMessage("Congratulations! You've been upgraded to Tier " + eligibleTier.getName());
            notification.setDetails("{}");

            notificationService.create(notification);
        }

        return orderDAO.update(order);
    }

    public Optional<Order> recalculateOrder(String orderId) {
        Optional<Order> orderOpt = orderDAO.getById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        System.out.println("Order : " + order);

        List<OrderItem> orderItems = orderItemDAO.getAllForOrder(orderId);

        double subtotal = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();

        double totalSavings = orderItems.stream()
                .mapToDouble(item -> item.getDiscountApplied() != null ? item.getDiscountValue() : 0.0)
                .sum();

        double totalAmount = subtotal + order.getDeliveryFee() - totalSavings;

        // Extract item IDs
        List<String> itemIds = orderItems.stream()
                .map(OrderItem::getId)
                .collect(Collectors.toList());

        // Set updated values
        order.setSubtotal(subtotal);
        order.setTotalSavings(totalSavings);
        order.setTotalAmount(totalAmount);
        order.setItems(JsonUtils.toJson(itemIds)); // Assuming order.items is a JSON string

        System.out.println("Updated Data: subtotal=" + subtotal + ", totalSavings=" + totalSavings + ", totalAmount=" + totalAmount);

        return orderDAO.update(order);
    }

}
