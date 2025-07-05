package org.example.service;

import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.enumtype.WorkspaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService();

        Workspace workspace = new Workspace("Room A", WorkspaceType.PRIVATE, 50.0);
        workspace.setId(1);
        WorkspaceService workspaceService = new WorkspaceService();
        workspaceService.createWorkspace(workspace);
    }

    @Test
    void testMakeReservationAddsReservation() throws ParseException {
        Interval interval = buildInterval("2025-07-05 09:00", "2025-07-05 11:00");

        reservationService.makeReservation("John", 1, interval);

        List<Reservation> allReservations = reservationService.getAllReservations();
        assertEquals(1, allReservations.size());

        Reservation saved = allReservations.get(0);
        assertEquals("John", saved.getName());
        assertEquals(1, saved.getSpaceId());
        assertEquals(interval.getStartTime(), saved.getInterval().getStartTime());
        assertEquals(interval.getEndTime(), saved.getInterval().getEndTime());
    }

    @Test
    void testCancelReservationRemovesReservation() throws ParseException {
        Interval interval = buildInterval("2025-07-06 14:00", "2025-07-06 16:00");

        reservationService.makeReservation("Alice", 1, interval);
        int reservationId = reservationService.getAllReservations().get(0).getId();

        reservationService.cancelReservation(reservationId);

        List<Reservation> remaining = reservationService.getAllReservations();
        assertTrue(remaining.isEmpty());
    }

    private Interval buildInterval(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT);
        return new Interval.IntervalBuilder()
                .startTime(sdf.parse(start))
                .endTime(sdf.parse(end))
                .build();
    }
}