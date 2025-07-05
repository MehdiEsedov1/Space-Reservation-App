package org.example.service;

import org.example.persistence.ReservationDAO;
import org.example.entity.Interval;
import org.example.entity.Reservation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationService {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final WorkspaceService workspaceService = new WorkspaceService();

    public Optional<Reservation> findReservationById(int id) {
        return reservationDAO.getAllReservations().stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst();
    }

    public Reservation getReservationById(int id) {
        return findReservationById(id).orElse(null);
    }

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservationDAO.getAllReservations());
    }

    public void makeReservation(String name, int spaceId, Interval interval) {
        if (!workspaceService.workspaceExists(spaceId)) {
            System.out.println("Workspace not found!");
            return;
        }

        if (!workspaceService.isWorkspaceAvailable(spaceId, interval)) {
            System.out.println("Workspace is not available!");
            return;
        }

        Reservation reservation = new Reservation(name, spaceId, interval);
        reservationDAO.saveReservation(reservation);

        System.out.println("Reservation created successfully!");
    }

    public List<Reservation> getReservationsForWorkspace(int workspaceId) {
        return reservationDAO.getAllReservations().stream()
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
