package org.example.persistence.DAO;

import jakarta.persistence.EntityManager;
import org.example.entity.User;
import org.example.enumtype.UserRole;
import org.example.persistence.JpaUtil;

import java.util.Optional;

public class UserDAO {

    public Optional<User> findByUsername(String username) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            User user = em.find(User.class, username);
            return Optional.ofNullable(user);
        } finally {
            em.close();
        }
    }

    public void save(User user) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void delete(String username) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            User user = em.find(User.class, username);
            if (user != null) {
                em.getTransaction().begin();
                em.remove(user);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    public void updateRole(String username, UserRole newRole) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            User user = em.find(User.class, username);
            if (user != null) {
                em.getTransaction().begin();
                user.setRole(newRole);
                em.merge(user);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    public boolean exists(String username) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(User.class, username) != null;
        } finally {
            em.close();
        }
    }
}
