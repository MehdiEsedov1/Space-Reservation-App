package org.example.ui;

import org.example.entity.Workspace;
import org.example.enumtype.WorkspaceType;
import org.example.service.WorkspaceService;

import java.util.List;

import static org.example.ui.util.ConsoleReader.*;

public class AdminMenuConsole {
    private static final WorkspaceService workspaceService = new WorkspaceService();
    private static final WorkspaceDisplayConsole workspaceConsole = new WorkspaceDisplayConsole();
    private static final ReservationDisplayConsole reservationConsole = new ReservationDisplayConsole();

    public void menu() {
        System.out.println("\n== Welcome to the ADMIN CONSOLE ==");
        boolean active = true;
        while (active) {
            String option = readLine("""
                    \nPlease select an option:
                    1 - Create a new workspace
                    2 - Edit a workspace
                    3 - Delete a workspace
                    4 - List all workspaces
                    5 - List all available workspaces
                    6 - List all reservations

                    0 - Back

                    >\s""");

            switch (option) {
                case "1" -> createWorkspace();
                case "2" -> editWorkspace();
                case "3" -> deleteWorkspace();
                case "4" -> workspaceConsole.listWorkspaces();
                case "5" -> workspaceConsole.listAvailableWorkspaces();
                case "6" -> reservationConsole.listReservations();
                case "0" -> active = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void createWorkspace() {
        System.out.println("\n== Create a new workspace ==\n");

        String name = readLine("Enter workspace name: ");
        int typeNo = readInt("Enter workspace type (1 - OPEN; 2 - PRIVATE; 3 - ROOM): ");
        WorkspaceType type = getType(typeNo);
        if (type == null) return;

        double price = readDouble("Enter workspace price: ");
        workspaceService.createWorkspace(new Workspace(name, type, price));
    }

    private void editWorkspace() {
        System.out.println("\n== Edit a workspace ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("Nothing to edit!");
            return;
        }

        workspaceConsole.printWorkspaces(workspaces);
        int id = readInt("Enter workspace ID to edit (0 - Cancel): ");
        if (id == 0) return;

        Workspace workspace = workspaceService.getWorkspaceByIdOrThrow(id);
        if (workspace == null) {
            System.out.println("Workspace not found!");
            return;
        }

        String nameInput = readLine("Enter new workspace name [" + workspace.getName() + "] (Enter to keep the same): ");
        if (!nameInput.isBlank()) {
            workspace.setName(nameInput);
        }

        String typeInput = readLine("Enter new workspace type [" + workspace.getType() + "]\n(1 - OPEN; 2 - PRIVATE; 3 - ROOM)\nEnter to keep the same: ");
        if (!typeInput.isBlank()) {
            try {
                WorkspaceType type = getType(Integer.parseInt(typeInput));
                if (type != null) {
                    workspace.setType(type);
                } else {
                    System.out.println("Invalid workspace type!");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid type format!");
                return;
            }
        }

        String priceInput = readLine("Enter new workspace price [" + workspace.getPrice() + "] (Enter to keep the same): ");
        if (!priceInput.isBlank()) {
            try {
                workspace.setPrice(Double.parseDouble(priceInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format!");
                return;
            }
        }

        workspaceService.editWorkspace(id, workspace);
    }

    private void deleteWorkspace() {
        System.out.println("\n== Delete a workspace ==\n");

        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        if (workspaces.isEmpty()) {
            System.out.println("Nothing to delete!");
            return;
        }

        workspaceConsole.printWorkspaces(workspaces);
        int id = readInt("Enter workspace ID to delete (0 - Cancel): ");
        if (id == 0) return;

        workspaceService.deleteWorkspace(id);
    }

    private WorkspaceType getType(int typeNo) {
        if (typeNo >= 1 && typeNo <= WorkspaceType.values().length) {
            return WorkspaceType.values()[typeNo - 1];
        }
        System.out.println("Invalid workspace type number: " + typeNo);
        return null;
    }
}
