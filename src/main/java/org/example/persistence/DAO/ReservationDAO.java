package org.example.persistence.DAO;

import jakarta.persistence.EntityManager;
import org.example.entity.Reservation;
import org.example.persistence.JpaUtil;

import java.util.List;

public class ReservationDAO {

    public void save(Reservation reservation) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(reservation);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public List<Reservation> findAll() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Reservation r", Reservation.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean deleteById(int id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            Reservation reservation = em.find(Reservation.class, id);
            if (reservation == null) return false;
            em.getTransaction().begin();
            em.remove(reservation);
            em.getTransaction().commit();
            return true;
        } finally {
            em.close();
        }
    }
}
