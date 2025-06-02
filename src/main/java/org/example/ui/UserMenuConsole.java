package org.example.ui;

import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.service.ReservationService;
import org.example.service.WorkspaceService;

import java.util.Date;
import java.util.List;

import static org.example.ui.util.ConsoleReader.*;

public class UserMenuConsole {
    private static final WorkspaceDisplayConsole workspaceConsole = new WorkspaceDisplayConsole();
    private static final ReservationDisplayConsole reservationConsole = new ReservationDisplayConsole();
    private static final IntervalInputConsole intervalConsole = new IntervalInputConsole();
    private static final ReservationService reservationService = new ReservationService();
    private static final WorkspaceService workspaceService = new WorkspaceService();

    public void menu() {
        System.out.println("\n== Welcome to the USER CONSOLE ==");
        boolean active = true;

        while (active) {
            String option = readLine("""
                    
                    Please select an option:
                    1 - List available workspaces
                    2 - Make reservation
                    3 - List all reservations
                    4 - Cancel reservation
                    
                    0 - Back
                    
                    > """);

            switch (option) {
                case "1" -> workspaceConsole.listAvailableWorkspaces();
                case "2" -> makeReservation();
                case "3" -> listAllReservations();
                case "4" -> cancelReservation();
                case "0" -> active = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void makeReservation() {
        System.out.println("\n== MAKE RESERVATION ==");

        Interval interval = intervalConsole.getInterval();
        if (interval == null || interval.getStartTime().before(new Date())) {
            System.out.println("Reservation can't be made in the past or is invalid!");
            return;
        }

        List<Workspace> availableWorkspaces = workspaceService.getAvailableWorkspaces(interval);
        if (availableWorkspaces.isEmpty()) {
            System.out.println("No available workspace to reserve!");
            return;
        }

        workspaceConsole.printWorkspaces(availableWorkspaces);
        int spaceId = readInt("Enter workspace ID to reserve (0 - Cancel): ");
        if (spaceId == 0) return;

        String name = readLine("Enter reservation name: ");
        reservationService.makeReservation(name, spaceId, interval);
    }

    private void listAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations found!");
            return;
        }

        System.out.println("\n== ALL RESERVATIONS ==\n");
        reservationConsole.printReservations(reservations);
    }

    private void cancelReservation() {
        System.out.println("\n== CANCEL RESERVATION ==");

        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservation to cancel!");
            return;
        }

        reservationConsole.printReservations(reservations);
        int id = readInt("Enter reservation ID to cancel (0 - Cancel): ");
        if (id == 0) return;

        reservationService.cancelReservation(id);
    }
}
