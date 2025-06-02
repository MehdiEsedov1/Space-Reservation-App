package org.example.ui;

import org.example.entity.Interval;
import org.example.entity.Workspace;
import org.example.service.WorkspaceService;

import java.util.Date;
import java.util.List;

public class WorkspaceDisplayConsole {
    private static final WorkspaceService workspaceService = new WorkspaceService();
    private static final IntervalInputConsole intervalConsole = new IntervalInputConsole();
    private static final String workspaceRowFormat = "%3d: %-30s %-10s %6.2f%n";

    public void listWorkspaces() {
        System.out.println("\n== ALL WORKSPACES ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        printWorkspaces(workspaces);
    }

    public void listAvailableWorkspaces() {
        System.out.println("\n== AVAILABLE WORKSPACES ==\n");
        System.out.println("Enter interval to check availability:");

        Interval interval = intervalConsole.getInterval();
        if (interval == null || interval.getStartTime().before(new Date())) {
            System.out.println("Invalid or past interval provided!");
            return;
        }

        List<Workspace> workspaces = workspaceService.getAvailableWorkspaces(interval);
        printWorkspaces(workspaces);
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
