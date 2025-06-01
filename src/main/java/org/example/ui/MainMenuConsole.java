package org.example.ui;

import static org.example.ui.util.ConsoleReader.*;

public class MainMenuConsole {
    private static final AdminMenuConsole adminConsole = new AdminMenuConsole();
    private static final UserMenuConsole userConsole = new UserMenuConsole();

    public void mainMenu() {
        System.out.println("\n== Welcome to the SPACE RESERVATION ==");
        boolean active = true;

        while (active) {
            String option = readLine("""
                    
                    Please login to the system:
                    1 - Admin login
                    2 - User login
                    
                    0 - Exit
                    
                    >\s""");

            switch (option) {
                case "1" -> adminConsole.menu();
                case "2" -> userConsole.menu();
                case "0" -> active = false;
                default -> System.out.println("Invalid option!");
            }
        }

        System.out.println("\nGoodbye!");
    }
}
