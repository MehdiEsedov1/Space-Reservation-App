package org.example.service;

import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.persistence.ReservationFileStorage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReservationService {
    private static final List<Reservation> reservations = new ArrayList<>();
    private static final WorkspaceService workspaceService = new WorkspaceService();
    private static int lastId = 0;

    public Optional<Reservation> findReservationById(int id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findFirst();
    }

    public Reservation getReservationById(int id) {
        return findReservationById(id).orElse(null);
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

        saveToFile();
    }

    public void cancelReservation(int id) {
        findReservationById(id).ifPresentOrElse(reservation -> {
            reservations.remove(reservation);
            System.out.println("Reservation cancelled successfully!");
            saveToFile();
        }, () -> System.out.println("Reservation not found!"));
    }

    public List<Reservation> getReservationsForWorkspace(int workspaceId) {
        return reservations.stream()
                .filter(r -> r.getSpaceId() == workspaceId)
                .collect(Collectors.toList());
    }

    public void loadFromFile() {
        try {
            List<Reservation> loaded = ReservationFileStorage.load();
            reservations.clear();
            reservations.addAll(loaded);
            lastId = reservations.stream()
                    .mapToInt(Reservation::getId)
                    .max()
                    .orElse(0);
            System.out.println("Reservations loaded from file.");
        } catch (IOException e) {
            System.out.println("ℹNo saved reservations file found. Starting fresh.");
        }
    }

    public void saveToFile() {
        try {
            ReservationFileStorage.save(reservations);
        } catch (IOException e) {
            System.out.println("⚠Failed to save reservations: " + e.getMessage());
        }
    }
}
