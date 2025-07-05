package org.example.persistence;

import org.example.entity.User;
import org.example.enumtype.UserRole;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/reservation";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mehdi";

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String roleStr = rs.getString("role");
                UserRole role = UserRole.valueOf(roleStr.toUpperCase());
                return Optional.of(new User(username, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(User user) {
        String sql = "INSERT INTO users (username, role) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getRole().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRole(String username, UserRole newRole) {
        String sql = "UPDATE users SET role = ? WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newRole.name());
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
