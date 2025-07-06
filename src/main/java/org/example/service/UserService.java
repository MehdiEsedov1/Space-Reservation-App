package org.example.service;

import org.example.persistence.DAO.UserDAO;
import org.example.entity.User;
import org.example.enumtype.UserRole;

import java.util.Optional;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void registerUser(String username, UserRole role) {
        if (userDAO.exists(username)) {
            System.out.println("Username already exists.");
            return;
        }
        userDAO.save(new User(username, role));
        System.out.println("User registered successfully.");
    }

    public Optional<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void changeUserRole(String username, UserRole newRole) {
        if (!userDAO.exists(username)) {
            System.out.println("User not found.");
            return;
        }
        userDAO.updateRole(username, newRole);
        System.out.println("Role updated successfully.");
    }

    public void deleteUser(String username) {
        if (!userDAO.exists(username)) {
            System.out.println("User not found.");
            return;
        }
        userDAO.delete(username);
        System.out.println("User deleted successfully.");
    }
}
