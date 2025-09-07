package com.example.membership_api.aspect;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.example.membership_api.model.User;
import com.example.membership_api.service.UserService;
import com.example.membership_api.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AdminOnlyAspect {

    @Autowired
    private UserService userService;

    @Around("@annotation(com.example.membership_api.annotations.AdminOnly)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();

        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: Invalid or missing token");
        }

        Optional<User> optionalUser = userService.getById(userId);

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: User not found");
        }

        User user = optionalUser.get();
        // System.out.printf("user: %s", user.isAdmin());
        if ( !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden: Admins only");
        }

        return joinPoint.proceed();
    }
}
