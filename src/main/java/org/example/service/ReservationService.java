package org.example.service;

import org.example.entity.Interval;
import org.example.entity.Reservation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReservationService {
    private static final List<Reservation> reservations = new ArrayList<>();
    private static final WorkspaceService workspaceService = new WorkspaceService();
    private static int lastId = 0;

    public Reservation getReservationById(int id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservations);
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
        reservation.setId(++lastId);
        reservations.add(reservation);

        System.out.println("Reservation created successfully!");
        System.out.println("Reservation ID: " + reservation.getId());
    }

    public void cancelReservation(int id) {
        Reservation reservation = getReservationById(id);
        if (reservation == null) {
            System.out.println("Reservation not found!");
            return;
        }

        reservations.remove(reservation);
        System.out.println("Reservation cancelled successfully!");
    }

    public List<Reservation> getReservationsForWorkspace(int workspaceId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getSpaceId() == workspaceId) {
                result.add(r);
            }
        }
        return result;
    }
}
