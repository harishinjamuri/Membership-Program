package com.example.membership_api.dao;

import com.example.membership_api.model.User;
import com.example.membership_api.constants.UserStatus;
import com.example.membership_api.constants.UserType;
import com.example.membership_api.dto.UserPage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    public UserPage getAll(int page, int perPage, Map<String, String> filters) {
        logger.debug("Fetching users with page: {}, perPage: {}, filters: {}", page, perPage, filters);

        String baseQuery = "SELECT u FROM User u WHERE 1=1";
        String countQuery = "SELECT COUNT(u) FROM User u WHERE 1=1";

        if (filters != null && filters.containsKey("status")) {
            baseQuery += " AND u.status = :status";
            countQuery += " AND u.status = :status";
        }

        if (filters != null && filters.containsKey("userType")) {
            baseQuery += " AND u.userType = :userType";
            countQuery += " AND u.userType = :userType";
        }

        TypedQuery<User> query = entityManager.createQuery(baseQuery, User.class);
        TypedQuery<Long> count = entityManager.createQuery(countQuery, Long.class);

        if (filters != null && filters.containsKey("status")) {
            try {
                UserStatus status = UserStatus.valueOf(filters.get("status").toUpperCase());
                query.setParameter("status", status);
                count.setParameter("status", status);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid status filter value: {}", filters.get("status"));
            }
        }

        if (filters != null && filters.containsKey("userType")) {
            try {
                UserType userType = UserType.valueOf(filters.get("status").toUpperCase());
                query.setParameter("userType", userType);
                count.setParameter("userType", userType);
            } catch (IllegalArgumentException e) {
                logger.error("Invalid userType filter value: {}", filters.get("userType"));
            }
        }

        query.setFirstResult((page - 1) * perPage);
        query.setMaxResults(perPage);

        List<User> results = query.getResultList();
        Long total = count.getSingleResult();

        logger.info("Fetched {} users out of total {}", results.size(), total);

        return new UserPage(results, total);
    }

    public Optional<User> getById(String userId) {
        logger.debug("Fetching user by ID: {}", userId);
        User user = entityManager.find(User.class, userId);
        if (user != null && user.getStatus() == UserStatus.ACTIVE) {
            logger.info("User found: {}", userId);
            return Optional.of(user);
        } else {
            logger.warn("User not found or inactive: {}", userId);
            return Optional.empty();
        }
    }

    public Optional<User> update(String userId, Map<String, Object> updates) {
        logger.debug("Updating user with ID: {} with data: {}", userId, updates);
        Optional<User> userOpt = getById(userId);

        if (userOpt.isEmpty()) {
            logger.warn("Update failed - user not found: {}", userId);
            return Optional.empty();
        }

        User user = userOpt.get();

        updates.forEach((key, value) -> {
            try {
                Field field = User.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(user, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("Failed to update field: {} on user: {}", key, userId, e);
            }
        });

        entityManager.merge(user);
        logger.info("User updated successfully: {}", userId);
        return Optional.of(user);
    }

    public boolean softDelete(String userId) {
        logger.debug("Soft deleting user with ID: {}", userId);
        Optional<User> userOpt = getById(userId);

        if (userOpt.isEmpty()) {
            logger.warn("Soft delete failed - user not found: {}", userId);
            return false;
        }

        User user = userOpt.get();
        user.setStatus(UserStatus.INACTIVE); // soft delete by setting status
        entityManager.merge(user);
        logger.info("User soft-deleted successfully: {}", userId);
        return true;
    }
}
