package com.example.membership_api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.membership_api.constants.OrderStatus;
import com.example.membership_api.model.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class OrderDAO {

    @PersistenceContext
    private EntityManager em;

    public List<Order> getAll(int page, int perPage, String status, String userId) {
        StringBuilder jpql = new StringBuilder("SELECT o FROM Order o WHERE 1=1");
        if (status != null) jpql.append(" AND o.status = :status");
        if (userId != null) jpql.append(" AND o.userId = :userId");

        TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class);
        if (status != null) query.setParameter("status", Enum.valueOf(OrderStatus.class, status));
        if (userId != null) query.setParameter("userId", userId);

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        return query.getResultList();
    }

    public long count(String status, String userId) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(o) FROM Order o WHERE 1=1");
        if (status != null) jpql.append(" AND o.status = :status");
        if (userId != null) jpql.append(" AND o.userId = :userId");

        TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
        if (status != null) query.setParameter("status", Enum.valueOf(OrderStatus.class, status));
        if (userId != null) query.setParameter("userId", userId);

        return query.getSingleResult();
    }

    public Optional<Order> getById(String id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    public Order create( Order order) {
        em.persist(order);
        return order;
    }

    public Optional<Order> update(Order order) {
        Order mergedOrder = em.merge(order);
        return Optional.ofNullable(mergedOrder);
    }

    // Delete order by ID
    public boolean delete(String id) {
        Order order = em.find(Order.class, id);
        if (order == null) {
            return false;
        }
        em.remove(order);
        return true;
    }

    public long getTotalOrders(String userId) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(o) FROM Order o WHERE o.userId = :userId", Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public double  getTotalSpend(String userId) {
        TypedQuery<Double> query = em.createQuery(
                "SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.userId = :userId",
                Double.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public Order save(Order order) {
        return em.merge(order);
    }
}
