package org.example.ui;

import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.service.ReservationService;
import org.example.service.WorkspaceService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationDisplayConsole {
    private static final WorkspaceService workspaceService = new WorkspaceService();
    private static final ReservationService reservationService = new ReservationService();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String rowFormat = "%3s: %-30s %-30s %-20s %-20s%n";

    public void listReservations() {
        System.out.println("\n== ALL RESERVATIONS ==\n");
        List<Reservation> reservations = reservationService.getAllReservations();
        printReservations(reservations);
    }

    public void printReservations(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found!");
            return;
        }

        System.out.printf(rowFormat, "ID", "Name", "Space Name", "Start Time", "End Time");

        for (Reservation reservation : reservations) {
            Workspace workspace = workspaceService.getWorkspaceByIdOrThrow(reservation.getSpaceId());
            String workspaceName = (workspace != null) ? workspace.getName() : "[deleted]";

            String startTime = reservation.getStartTime().format(formatter);
            String endTime = reservation.getEndTime().format(formatter);

            System.out.printf(rowFormat, reservation.getId(), reservation.getName(), workspaceName, startTime, endTime);
        }
    }
}
