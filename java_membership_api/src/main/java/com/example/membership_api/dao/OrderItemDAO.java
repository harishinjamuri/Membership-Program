package com.example.membership_api.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.OrderItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class OrderItemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<OrderItem> getAllForOrder(String orderId) {
        String jpql = "SELECT oi FROM OrderItem oi WHERE oi.orderId = :orderId";
        return entityManager.createQuery(jpql, OrderItem.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public OrderItem getById(String orderItemId) {
        return entityManager.find(OrderItem.class, orderItemId);
    }

    public List<OrderItem> getByIds(List<String> orderItemIds) {
        String jpql = "SELECT oi FROM OrderItem oi WHERE oi.id IN :ids";
        return entityManager.createQuery(jpql, OrderItem.class)
                .setParameter("ids", orderItemIds)
                .getResultList();
    }

    public OrderItem create(OrderItem orderItem) {
        entityManager.persist(orderItem);
        entityManager.flush();  // Force insert to DB and get generated id
        return orderItem;
    }

    public OrderItem update(OrderItem orderItem) {
        return entityManager.merge(orderItem);
    }

    public boolean delete(String orderItemId) {
        OrderItem oi = getById(orderItemId);
        if (oi == null) {
            return false;
        }
        entityManager.remove(oi);
        return true;
    }
}
