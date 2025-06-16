package org.example.service;

import org.example.entity.Interval;
import org.example.entity.Workspace;
import org.example.exception.WorkspaceNotFoundException;
import org.example.persistence.WorkspaceFileStorage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public final class WorkspaceService {
    private static final Map<Integer, Workspace> workspacesMap = new HashMap<>();
    private static final ReservationService reservationService = new ReservationService();
    private static int lastId = 0;

    public Optional<Workspace> findWorkspaceById(int id) {
        return Optional.ofNullable(workspacesMap.get(id));
    }

    public Workspace getWorkspaceByIdOrThrow(int id) {
        return Optional.ofNullable(workspacesMap.get(id))
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace not found for ID: " + id));
    }

    public List<Workspace> getAllWorkspaces() {
        return List.copyOf(workspacesMap.values());
    }

    public List<Workspace> getAvailableWorkspaces(Interval interval) {
        if (interval == null) return Collections.emptyList();

        return workspacesMap.values().stream()
                .filter(workspace -> isWorkspaceAvailable(workspace.getId(), interval))
                .collect(Collectors.toList());
    }

    public void createWorkspace(Workspace workspace) {
        workspace.setId(++lastId);
        workspacesMap.put(workspace.getId(), workspace);
        System.out.printf("Workspace created successfully!\nWorkspace ID: %d%n", workspace.getId());
        saveToFile();
    }

    public void editWorkspace(int id, Workspace updated) {
        findWorkspaceById(id).ifPresentOrElse(existing -> {
            updated.setId(id);
            workspacesMap.put(id, updated);
            System.out.println("Workspace updated successfully!");
            saveToFile();
        }, () -> System.out.println("Workspace not found for ID: " + id));
    }

    public void deleteWorkspace(int id) {
        findWorkspaceById(id).ifPresentOrElse(workspace -> {
            boolean hasFutureReservation = reservationService.getAllReservations().stream()
                    .anyMatch(reservation ->
                            reservation.getSpaceId() == id &&
                                    reservation.getInterval().getEndTime().after(new Date()));

            if (hasFutureReservation) {
                System.out.println("This workspace cannot be deleted because there are upcoming reservations!");
                return;
            }

            workspacesMap.remove(id);
            System.out.println("Workspace deleted successfully!");
            saveToFile();
        }, () -> System.out.println("Workspace not found for ID: " + id));
    }

    public boolean workspaceExists(int id) {
        return workspacesMap.containsKey(id);
    }

    public boolean isWorkspaceAvailable(int id, Interval interval) {
        return reservationService.getAllReservations().stream()
                .filter(r -> r.getSpaceId() == id)
                .noneMatch(r -> Interval.isOverlap(r.getInterval(), interval));
    }

    public void loadFromFile() {
        try {
            List<Workspace> loaded = WorkspaceFileStorage.load();
            workspacesMap.clear();
            loaded.forEach(ws -> workspacesMap.put(ws.getId(), ws));

            lastId = workspacesMap.keySet().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(0);

            System.out.println("Workspaces loaded from file.");
        } catch (IOException e) {
            System.out.println("No saved workspaces file found. Starting fresh.");
        }
    }

    public void saveToFile() {
        try {
            WorkspaceFileStorage.save(new ArrayList<>(workspacesMap.values()));
        } catch (IOException e) {
            System.out.println("Failed to save workspaces: " + e.getMessage());
        }
    }
}