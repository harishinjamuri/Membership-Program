package com.example.membership_api.utils;

import jakarta.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

import java.util.Date;
import java.util.Base64;

import javax.crypto.SecretKey;



public class JwtUtil {

    private static final String SECRET = "3jBVLh/1YypbGOJZB9iIVj1Rhcyd8hqcy9VVL+1UjvQ=";

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET));

    private static final int EXPIRATION = 86400000; // 1 day

    public static String generateToken(String userId) {
        return Jwts.builder()
            .setSubject(userId)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    public static String extractUserIdFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("access_token_cookie".equals(cookie.getName())) {
                try {
                    Claims claims = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(cookie.getValue())
                        .getBody();
                    return claims.getSubject();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static Cookie createCookie(String token) {
        Cookie cookie = new Cookie("access_token_cookie", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(EXPIRATION / 1000);
        return cookie;
    }

    public static Cookie clearCookie() {
        Cookie cookie = new Cookie("access_token_cookie", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }
}
