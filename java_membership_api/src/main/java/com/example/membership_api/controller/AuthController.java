package com.example.membership_api.controller;


import com.example.membership_api.model.User;
import com.example.membership_api.service.AuthService;
import com.example.membership_api.utils.DateUtils;
import com.example.membership_api.utils.JwtUtil;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);


    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {
        try {

            logger.info( "Service: body {}", body);

            User user = authService.register(
                (String) body.get("name"),
                (String) body.get("email"),
                (String) body.get("password"),
                (String) body.get("phone_number"),
                body.get("date_of_birth") != null
                    ? DateUtils.parseSlashDateToDateTime((String) body.get("date_of_birth"))
                    : null
            );

            return ResponseEntity.status(201).body(Map.of("message", "User registered","user", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
        User user = authService.login(body.get("email"), body.get("password"));
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));

        String token = JwtUtil.generateToken(user.getId());
        Cookie cookie = JwtUtil.createCookie(token);
        response.addCookie(cookie);

        authService.updateLastLogin(user.getId());
        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = JwtUtil.clearCookie();
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));

        var result = authService.changePassword(userId, body.get("current_password"), body.get("new_password"));
        if (result.getLeft() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", result.getRight()));
        }
        return ResponseEntity.ok(Map.of("message", "Password changed"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));

        User user = authService.getUserById(userId);
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "name", user.getName(),
            "dob", user.getDateOfBirth(),
            "phone", user.getPhoneNumber()
        ));
    }
}
