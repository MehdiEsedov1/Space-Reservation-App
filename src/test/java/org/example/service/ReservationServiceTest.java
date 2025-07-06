package org.example.service;

import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.enumtype.WorkspaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private WorkspaceService workspaceService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService();
        workspaceService = new WorkspaceService();

        Workspace workspace = new Workspace("Room A", WorkspaceType.PRIVATE, 50.0);
        workspace.setId(1);
        workspaceService.createWorkspace(workspace);
    }

    @Test
    void testMakeReservationAddsReservation() {
        LocalDateTime[] interval = buildStartEnd("2025-07-05 09:00", "2025-07-05 11:00");

        reservationService.makeReservation("John", 1, interval[0], interval[1]);

        List<Reservation> allReservations = reservationService.getAllReservations();
        assertEquals(1, allReservations.size());

        Reservation saved = allReservations.get(0);
        assertEquals("John", saved.getName());
        assertEquals(1, saved.getSpaceId());
        assertEquals(interval[0], saved.getStartTime());
        assertEquals(interval[1], saved.getEndTime());
    }

    @Test
    void testCancelReservationRemovesReservation() {
        LocalDateTime[] interval = buildStartEnd("2025-07-06 14:00", "2025-07-06 16:00");

        reservationService.makeReservation("Alice", 1, interval[0], interval[1]);
        int reservationId = reservationService.getAllReservations().get(0).getId();

        reservationService.cancelReservation(reservationId);

        List<Reservation> remaining = reservationService.getAllReservations();
        assertTrue(remaining.isEmpty());
    }

    private LocalDateTime[] buildStartEnd(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        return new LocalDateTime[]{startTime, endTime};
    }
}
