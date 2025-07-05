package org.example.service;

import org.example.persistence.ReservationDAO;
import org.example.persistence.WorkspaceDAO;
import org.example.entity.Interval;
import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.exception.WorkspaceNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class WorkspaceService {
    private final WorkspaceDAO workspaceDAO = new WorkspaceDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    public List<Workspace> getAllWorkspaces() {
        return workspaceDAO.findAll();
    }

    public Optional<Workspace> findWorkspaceById(int id) {
        return workspaceDAO.findById(id);
    }

    public Workspace getWorkspaceByIdOrThrow(int id) {
        return findWorkspaceById(id).orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found for ID: " + id));
    }

    public List<Workspace> getAvailableWorkspaces(Interval interval) {
        if (interval == null) return List.of();
        return workspaceDAO.findAll().stream()
                .filter(workspace -> isWorkspaceAvailable(workspace.getId(), interval))
                .collect(Collectors.toList());
    }

    public void createWorkspace(Workspace workspace) {
        workspaceDAO.saveWorkspace(workspace);
        System.out.println("Workspace created successfully!");
    }

    public void editWorkspace(int id, Workspace updated) {
        if (workspaceDAO.updateWorkspace(id, updated)) {
            System.out.println("Workspace updated successfully!");
        } else {
            System.out.println("Workspace not found for ID: " + id);
        }
    }

    public void deleteWorkspace(int id) {
        List<Reservation> all = reservationDAO.getAllReservations();
        boolean hasFutureReservation = all.stream()
                .anyMatch(reservation ->
                        reservation.getSpaceId() == id &&
                                reservation.getInterval().getEndTime().after(new Date()));

        if (hasFutureReservation) {
            System.out.println("This workspace cannot be deleted because there are upcoming reservations!");
            return;
        }

        if (workspaceDAO.deleteWorkspace(id)) {
            System.out.println("Workspace deleted successfully!");
        } else {
            System.out.println("Workspace not found for ID: " + id);
        }
    }

    public boolean workspaceExists(int id) {
        return workspaceDAO.findById(id).isPresent();
    }

    public boolean isWorkspaceAvailable(int id, Interval interval) {
        return reservationDAO.getAllReservations().stream()
                .filter(r -> r.getSpaceId() == id)
                .noneMatch(r -> Interval.isOverlap(r.getInterval(), interval));
    }
}
