package org.example.entity;

import org.example.enumtype.UserRole;

import java.util.Objects;

public class User {
    private final String username;
    private final UserRole role;

    public User(String username, UserRole role) {
        this.username = Objects.requireNonNull(username, "Username cannot be null.");
        this.role = Objects.requireNonNull(role, "User role cannot be null.");
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
