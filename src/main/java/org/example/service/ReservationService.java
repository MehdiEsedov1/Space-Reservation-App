package org.example.service;

import org.example.entity.Reservation;
import org.example.persistence.DAO.ReservationDAO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final WorkspaceService workspaceService = new WorkspaceService();

    public Optional<Reservation> findReservationById(int id) {
        return reservationDAO.findAll().stream()
                .filter(r -> r.getId() == id)
                .findFirst();
    }

    public Reservation getReservationById(int id) {
        return findReservationById(id).orElse(null);
    }

    public List<Reservation> getAllReservations() {
        return reservationDAO.findAll();
    }

    public void makeReservation(String name, int spaceId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!workspaceService.workspaceExists(spaceId)) {
            System.out.println("Workspace not found!");
            return;
        }

        if (!workspaceService.isWorkspaceAvailable(spaceId, startTime, endTime)) {
            System.out.println("Workspace is not available!");
            return;
        }

        Reservation reservation = new Reservation(name, spaceId, startTime, endTime);
        reservationDAO.save(reservation);

        System.out.println("Reservation created successfully!");
    }

    public List<Reservation> getReservationsForWorkspace(int workspaceId) {
        return reservationDAO.findAll().stream()
                .filter(r -> r.getSpaceId() == workspaceId)
                .collect(Collectors.toList());
    }

    public void cancelReservation(int id) {
        boolean deleted = reservationDAO.deleteById(id);
        if (deleted) {
            System.out.println("Reservation cancelled successfully!");
        } else {
            System.out.println("Reservation not found!");
        }
    }
}
