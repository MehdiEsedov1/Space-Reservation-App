package org.example.service;

import org.example.entity.Reservation;
import org.example.entity.Workspace;
import org.example.exception.WorkspaceNotFoundException;
import org.example.persistence.DAO.ReservationDAO;
import org.example.persistence.DAO.WorkspaceDAO;

import java.time.LocalDateTime;
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
        return findWorkspaceById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found for ID: " + id));
    }

    public List<Workspace> getAvailableWorkspaces(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null || !startTime.isBefore(endTime)) return List.of();
        return workspaceDAO.findAll().stream()
                .filter(workspace -> isWorkspaceAvailable(workspace.getId(), startTime, endTime))
                .collect(Collectors.toList());
    }

    public void createWorkspace(Workspace workspace) {
        workspaceDAO.save(workspace);
        System.out.println("Workspace created successfully!");
    }

    public void editWorkspace(int id, Workspace updated) {
        if (workspaceDAO.update(id, updated)) {
            System.out.println("Workspace updated successfully!");
        } else {
            System.out.println("Workspace not found for ID: " + id);
        }
    }

    public void deleteWorkspace(int id) {
        List<Reservation> all = reservationDAO.findAll();
        boolean hasFutureReservation = all.stream()
                .anyMatch(reservation ->
                        reservation.getSpaceId() == id &&
                                reservation.getEndTime().isAfter(LocalDateTime.now()));

        if (hasFutureReservation) {
            System.out.println("This workspace cannot be deleted because there are upcoming reservations!");
            return;
        }

        if (workspaceDAO.delete(id)) {
            System.out.println("Workspace deleted successfully!");
        } else {
            System.out.println("Workspace not found for ID: " + id);
        }
    }

    public boolean workspaceExists(int id) {
        return workspaceDAO.findById(id).isPresent();
    }

    public boolean isWorkspaceAvailable(int id, LocalDateTime start, LocalDateTime end) {
        return reservationDAO.findAll().stream()
                .filter(r -> r.getSpaceId() == id)
                .noneMatch(r ->
                        r.getStartTime().isBefore(end) &&
                                r.getEndTime().isAfter(start));
    }
}
