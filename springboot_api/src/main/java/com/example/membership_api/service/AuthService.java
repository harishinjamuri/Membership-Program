package com.example.membership_api.service;

import java.time.LocalDateTime;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.controller.HealthController;
import com.example.membership_api.dao.AuthDAO;
import com.example.membership_api.model.User;
import com.example.membership_api.utils.PasswordUtils;




@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @Autowired
    private AuthDAO authDAO;

    public User register(String name, String email, String password, String phone, LocalDateTime dob) {
        if (authDAO.getByEmail(email) != null) {
            throw new IllegalArgumentException("User already exists");
        }

        logger.info( "Service: name {}, email {}, phone {}, dob {}, password {} Hash {}", name, email, phone, dob, password, PasswordUtils.hashPassword(password) );

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(PasswordUtils.hashPassword(password));
        user.setPhoneNumber(phone);
        user.setDateOfBirth(dob);
        user.setCreatedAt(LocalDateTime.now());
        logger.info("user {}", user.toString());
        return authDAO.createUser(user);
    }

    public User login(String email, String password) {
        User user = authDAO.getByEmail(email);
        if (user != null && PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public void updateLastLogin(String userId) {
        User user = authDAO.getById(userId);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
        }
    }

    public User getUserById(String id) {
        return authDAO.getById(id);
    }

    public Pair<User, String> changePassword(String id, String currentPassword, String newPassword) {
        User user = authDAO.getById(id);
        if (user == null) return Pair.of(null, "User not found");

        if (!PasswordUtils.verifyPassword(currentPassword, user.getPasswordHash())) {
            return Pair.of(null, "Incorrect current password");
        }

        User updated = authDAO.changePassword(id, PasswordUtils.hashPassword(newPassword));
        return Pair.of(updated, null);
    }
}
