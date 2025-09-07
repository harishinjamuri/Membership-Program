package com.example.membership_api.dao;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.example.membership_api.constants.UserStatus;
import com.example.membership_api.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Repository
@Transactional
public class AuthDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public User getByEmail(String email) {
        String q = "SELECT u FROM User u WHERE u.email = :email";
        return entityManager.createQuery(q, User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public User getById(String id) {
        return entityManager.find(User.class, id);
    }

    public User createUser(User user) {
        System.out.printf("user: {}",user.toString());
        entityManager.persist(user);
        return user;
    }

    public User changePassword(String userId, String newPasswordHash) {
        User user = getById(userId);
        if (user != null && user.getStatus() == UserStatus.ACTIVE) {
            user.setPasswordHash(newPasswordHash);
            user.setUpdatedAt(LocalDateTime.now());
            entityManager.merge(user);
            return user;
        }
        return null;
    }
}
