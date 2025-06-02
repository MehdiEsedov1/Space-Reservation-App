package org.example.service;

import org.example.entity.Interval;
import org.example.entity.Workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class WorkspaceService {
    private static final List<Workspace> workspaces = new ArrayList<>();
    private static final ReservationService reservationService = new ReservationService();
    private static int lastId = 0;

    public Workspace getWorkspaceById(int id) {
        return workspaces.stream()
                .filter(workspace -> workspace.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Workspace> getAllWorkspaces() {
        return Collections.unmodifiableList(workspaces);
    }

    public List<Workspace> getAvailableWorkspaces(Interval interval) {
        if (interval == null) return Collections.emptyList();

        return workspaces.stream()
                .filter(workspace -> isWorkspaceAvailable(workspace.getId(), interval))
                .collect(Collectors.toList());
    }

    public void createWorkspace(Workspace workspace) {
        int id = ++lastId;
        workspace.setId(id);
        workspaces.add(workspace);
        System.out.println("Workspace created successfully!");
        System.out.println("Workspace ID: " + id);
    }

    public void editWorkspace(int id, Workspace updated) {
        Workspace existing = getWorkspaceById(id);

        if (existing == null) {
            System.out.println("Workspace not found!");
            return;
        }

        updated.setId(id);
        workspaces.set(workspaces.indexOf(existing), updated);
        System.out.println("Workspace updated successfully!");
    }

    public void deleteWorkspace(int id) {
        boolean hasFutureReservation = reservationService.getAllReservations().stream()
                .anyMatch(reservation ->
                        reservation.getSpaceId() == id &&
                                reservation.getInterval().getEndTime().after(new Date()));

        if (hasFutureReservation) {
            System.out.println("This workspace cannot be deleted because there are upcoming reservations!");
            return;
        }

        boolean removed = workspaces.removeIf(workspace -> workspace.getId() == id);
        if (removed) {
            System.out.println("Workspace deleted successfully!");
        } else {
            System.out.println("Workspace not found!");
        }
    }

    public boolean workspaceExists(int id) {
        return workspaces.stream().anyMatch(workspace -> workspace.getId() == id);
    }

    public boolean isWorkspaceAvailable(int id, Interval interval) {
        return reservationService.getAllReservations().stream()
                .filter(r -> r.getSpaceId() == id)
                .noneMatch(r -> Interval.isOverlap(r.getInterval(), interval));
    }
}
