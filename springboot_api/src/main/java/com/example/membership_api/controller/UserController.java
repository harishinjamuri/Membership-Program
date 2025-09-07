package com.example.membership_api.controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.annotations.AdminOnly;
import com.example.membership_api.dto.UserPage;
import com.example.membership_api.model.User;
import com.example.membership_api.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @AdminOnly
    public ResponseEntity<UserPage> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam Map<String, String> filters) {

        logger.debug("Controller: getAll called with page {}, perPage {}, filters {}", page, perPage, filters);

        // Remove pagination params from filters map so DAO only gets actual filters
        filters.remove("page");
        filters.remove("perPage");

        UserPage userPage = userService.getAll(page, perPage, filters);
        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getOne(@PathVariable("userId") String userId) {
        logger.debug("Controller: getOne called with userId {}", userId);

        Optional<User> userOpt = userService.getById(userId);
        return userOpt
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> update(@PathVariable("userId") String userId,
                                       @RequestBody Map<String, Object> updates) {
        logger.debug("Controller: update called with userId {}, updates {}", userId, updates);

        Optional<User> updatedUser = userService.update(userId, updates);
        return updatedUser
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/users/{user_id}
    @AdminOnly
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable("userId") String userId) {
        logger.debug("Controller: delete called with userId {}", userId);

        boolean deleted = userService.softDelete(userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
