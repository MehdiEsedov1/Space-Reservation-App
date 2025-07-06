package org.example.ui;

import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.service.ReservationService;
import org.example.service.WorkspaceService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.example.ui.util.ConsoleReader.*;

public class UserMenuConsole {
    private static final WorkspaceDisplayConsole workspaceConsole = new WorkspaceDisplayConsole();
    private static final ReservationDisplayConsole reservationConsole = new ReservationDisplayConsole();
    private static final ReservationService reservationService = new ReservationService();
    private static final WorkspaceService workspaceService = new WorkspaceService();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        String dateStr = readLine("Enter date (yyyy-MM-dd): ");
        String startStr = readLine("Enter start time (HH:mm): ");
        String endStr = readLine("Enter end time (HH:mm): ");

        if (dateStr.isBlank() || startStr.isBlank() || endStr.isBlank()) {
            System.out.println("Date and time inputs cannot be empty!");
            return;
        }

        try {
            LocalDateTime start = LocalDateTime.parse(dateStr + " " + startStr, formatter);
            LocalDateTime end = LocalDateTime.parse(dateStr + " " + endStr, formatter);

            if (!start.isBefore(end) || start.isBefore(LocalDateTime.now())) {
                System.out.println("Invalid reservation time (either in the past or end before start).");
                return;
            }

            List<Workspace> availableWorkspaces = workspaceService.getAvailableWorkspaces(start, end);
            if (availableWorkspaces.isEmpty()) {
                System.out.println("No available workspace to reserve!");
                return;
            }

            workspaceConsole.printWorkspaces(availableWorkspaces);
            int spaceId = readInt("Enter workspace ID to reserve (0 - Cancel): ");
            if (spaceId == 0) return;

            String name = readLine("Enter reservation name: ");
            reservationService.makeReservation(name, spaceId, start, end);

        } catch (Exception e) {
            System.out.println("Invalid date or time format. Use yyyy-MM-dd and HH:mm");
        }
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
