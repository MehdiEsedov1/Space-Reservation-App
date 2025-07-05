package org.example.service;

import org.example.entity.User;
import org.example.enumtype.UserRole;
import org.junit.jupiter.api.*;
import org.example.persistence.DAO.UserDAO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {

    private static final UserDAO userDAO = new UserDAO();
    private static final String TEST_USERNAME = "testuser";

    @Test
    @Order(1)
    void testSaveUser() {
        User user = new User(TEST_USERNAME, UserRole.USER);
        userDAO.save(user);
        assertTrue(userDAO.exists(TEST_USERNAME));
    }

    @Test
    @Order(2)
    void testFindByUsername() {
        Optional<User> result = userDAO.findByUsername(TEST_USERNAME);
        assertTrue(result.isPresent());
        assertEquals(TEST_USERNAME, result.get().getUsername());
        assertEquals(UserRole.USER, result.get().getRole());
    }

    @Test
    @Order(3)
    void testUpdateRole() {
        userDAO.updateRole(TEST_USERNAME, UserRole.ADMIN);
        Optional<User> updated = userDAO.findByUsername(TEST_USERNAME);
        assertTrue(updated.isPresent());
        assertEquals(UserRole.ADMIN, updated.get().getRole());
    }

    @Test
    @Order(4)
    void testDeleteUser() {
        userDAO.delete(TEST_USERNAME);
        assertFalse(userDAO.exists(TEST_USERNAME));
    }
}
