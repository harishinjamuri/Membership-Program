package com.example.membership_api.model;

import java.time.LocalDateTime;

import com.example.membership_api.constants.UserStatus;
import com.example.membership_api.constants.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
public class User extends BaseModel {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name="last_login")
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    @Column(name="user_type",nullable = false, updatable = false)
    private UserType userType = UserType.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name="password_hash", nullable = false)
    private String passwordHash;

    // isAdmin helper method
    public boolean isAdmin() {
        return userType == UserType.ADMIN;
    }

}
