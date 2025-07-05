package org.example.ui;

import org.example.entity.Workspace;
import org.example.service.WorkspaceService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.example.ui.util.ConsoleReader.*;

public class WorkspaceDisplayConsole {
    private static final WorkspaceService workspaceService = new WorkspaceService();
    private static final String workspaceRowFormat = "%3d: %-30s %-10s %6.2f%n";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void listWorkspaces() {
        System.out.println("\n== ALL WORKSPACES ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        printWorkspaces(workspaces);
    }

    public void listAvailableWorkspaces() {
        System.out.println("\n== AVAILABLE WORKSPACES ==\n");

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
                System.out.println("Invalid interval. Start must be before end and not in the past.");
                return;
            }

            List<Workspace> workspaces = workspaceService.getAvailableWorkspaces(start, end);
            printWorkspaces(workspaces);

        } catch (Exception e) {
            System.out.println("Invalid format. Use yyyy-MM-dd for date and HH:mm for time.");
        }
    }

    public void printWorkspaces(List<Workspace> workspaces) {
        if (workspaces == null || workspaces.isEmpty()) {
            System.out.println("No workspaces found!");
            return;
        }

        System.out.printf("%3s: %-30s %-10s %s%n", "ID", "Name", "Type", "Price");
        for (Workspace workspace : workspaces) {
            System.out.printf(workspaceRowFormat,
                    workspace.getId(),
                    workspace.getName(),
                    workspace.getType(),
                    workspace.getPrice());
        }
    }
}
