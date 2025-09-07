package com.example.membership_api.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<?> success(String message, Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("data", data);
        body.put("success", true);
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<?> success(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("success", true);  // fixed to true
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<?> error(String message, int statusCode) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("success", false);
        return ResponseEntity.status(statusCode).body(body);
    }

    // Optional overload for default 400 Bad Request
    public static ResponseEntity<?> error(String message) {
        return error(message, HttpStatus.BAD_REQUEST.value());
    }
}
