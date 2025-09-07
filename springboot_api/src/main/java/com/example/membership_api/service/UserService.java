package com.example.membership_api.service;

import com.example.membership_api.dao.UserDAO;
import com.example.membership_api.dto.UserPage;
import com.example.membership_api.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserPage getAll(int page, int perPage, Map<String, String> filters) {
        logger.debug("Service: Fetching users with page: {}, perPage: {}, filters: {}", page, perPage, filters);
        UserPage userPage = userDAO.getAll(page, perPage, filters);
        logger.info("Service: Fetched {} users out of total {}", userPage.getUsers().size(), userPage.getTotal());
        return userPage;
    }

    public Optional<User> getById(String userId) {
        logger.debug("Service: Fetching user by ID: {}", userId);
        Optional<User> userOpt = userDAO.getById(userId);

        if (userOpt.isPresent()) {
            logger.info("Service: User found with ID: {}", userId);
        } else {
            logger.warn("Service: User not found or inactive with ID: {}", userId);
        }

        return userOpt;
    }

    public Optional<User> update(String userId, Map<String, Object> updates) {
        logger.debug("Service: Updating user with ID: {} with data: {}", userId, updates);
        Optional<User> updatedUser = userDAO.update(userId, updates);

        if (updatedUser.isPresent()) {
            logger.info("Service: User updated successfully: {}", userId);
        } else {
            logger.warn("Service: Update failed, user not found: {}", userId);
        }

        return updatedUser;
    }

    public boolean softDelete(String userId) {
        logger.debug("Service: Soft deleting user with ID: {}", userId);
        boolean deleted = userDAO.softDelete(userId);

        if (deleted) {
            logger.info("Service: User soft-deleted successfully: {}", userId);
        } else {
            logger.warn("Service: Soft delete failed, user not found: {}", userId);
        }

        return deleted;
    }
}
