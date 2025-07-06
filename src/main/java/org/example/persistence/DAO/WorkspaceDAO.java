package org.example.persistence.DAO;

import jakarta.persistence.EntityManager;
import org.example.entity.Workspace;
import org.example.persistence.JpaUtil;

import java.util.List;
import java.util.Optional;

public class WorkspaceDAO {

    public void save(Workspace workspace) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(workspace);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Workspace> findAll() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT w FROM Workspace w", Workspace.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Workspace> findById(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return Optional.ofNullable(em.find(Workspace.class, id));
        } finally {
            em.close();
        }
    }

    public boolean update(int id, Workspace updated) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            Workspace existing = em.find(Workspace.class, id);
            if (existing == null) return false;

            em.getTransaction().begin();
            existing.setName(updated.getName());
            existing.setType(updated.getType());
            existing.setPrice(updated.getPrice());
            em.merge(existing);
            em.getTransaction().commit();
            return true;
        } finally {
            em.close();
        }
    }

    public boolean delete(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            Workspace workspace = em.find(Workspace.class, id);
            if (workspace == null) return false;

            em.getTransaction().begin();
            em.remove(workspace);
            em.getTransaction().commit();
            return true;
        } finally {
            em.close();
        }
    }
}
