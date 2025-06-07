package org.example.ui.util;

import java.util.Scanner;

public final class ConsoleReader {
    private static final Scanner scanner = new Scanner(System.in);

    private ConsoleReader() {
    }

    public static String readLine() {
        return scanner.nextLine().trim();
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return readLine();
    }

    public static int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    public static int readInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    public static double readDouble() {
        while (true) {
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    public static double readDouble(String prompt) {
        System.out.print(prompt);
        return readDouble();
    }
}
