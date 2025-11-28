package View;

import Controller.*;
import java.io.Console;
import java.util.Scanner;

/**
 * Command-line interface view for the timetabling system.
 * This class implements AutoCloseable to ensure the Scanner resource is properly managed.
 * It handles all user input and output for the CLI and serves as the main entry point
 * for the application.
 */
public class CLIView implements AutoCloseable {

    /**
     * Scanner object used to read user input from the console (System.in).
     */
    private final Scanner scanner;

    /**
     * Constructs a new CLIView and initializes the Scanner object for input.
     */
    public CLIView() {
        // We use System.in directly as it's the standard for CLIs
        this.scanner = new Scanner(System.in);
    }

    /**
     * Closes the underlying Scanner resource to prevent resource leaks.
     * This method should be called when the application shuts down.
     */
    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

    // --- Main Entry Point ---

    /**
     * The main entry point for the UL Timetabling System.
     * It initializes the main controller, starts the application loop, and ensures
     * the view resources are closed properly.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("--- UL Timetabling System ---");
        try (CLIView view = new CLIView()) {
            // Pass the initialized view instance to the controller
            AppController controller = new AppController(view);
            controller.run();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during application shutdown: " + e.getMessage());
        }
    }

    // --- Output Methods ---

    // (displayMessage, displaySeparator, displayTimetable methods remain the same)

    /**
     * Displays a general message to the user console.
     *
     * @param message The string message to be displayed.
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints a standard separator line to visually divide sections of output.
     */
    public void displaySeparator() {
        System.out.println("------------------------------------");
    }

    /**
     * Displays a formatted timetable string, wrapped by separators.
     *
     * @param timetable The pre-formatted string representation of the schedule/timetable.
     */
    public void displayTimetable(String timetable) {
        displaySeparator();
        System.out.println("SCHEDULE:\n" + timetable);
        displaySeparator();
    }

    // --- Input Methods ---

    /**
     * Requests and returns login credentials (email and password) from the user.
     * Attempts to use System.console() for secure password input, falling back to
     * Scanner if the console is unavailable (e.g., running in an IDE).
     *
     * @return A String array containing two elements: {email, password}.
     */
    public String[] requestLogin() {
        displaySeparator();
        System.out.println("Please Login");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        String password;
        Console console = System.console();

        if (console != null) {
            // Securely read password (will not echo characters)
            System.out.print("Password: ");
            char[] passwordArray = console.readPassword();
            password = new String(passwordArray);
        } else {
            // Fallback for IDEs (insecure)
            System.out.print("Password (Warning: characters will be visible): ");
            password = scanner.nextLine();
        }

        return new String[]{email, password};
    }

    /**
     * Prompts the user for a general string input.
     *
     * @param prompt The message displayed to the user before waiting for input.
     * @return The string entered by the user.
     */
    public String requestInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Prompts the user for an integer choice and handles invalid number input.
     * The method continuously loops until a valid integer is entered.
     *
     * @param prompt The message displayed to the user before waiting for input.
     * @return The integer value entered by the user.
     */
    public int getUserChoice(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String line = scanner.nextLine();
                // Check if the user is trying to exit the input loop by providing '0'
                if (line.trim().isEmpty()) {
                    System.out.println("Input cannot be empty.");
                    continue;
                }
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Displays the main menu options based on the provided user role and prompts for a choice.
     *
     * NOTE: The extensive role-based logic here violates strict MVC. In a professional app,
     * the Controller should pass the full menu text/options to this method.
     *
     * @param role The role of the currently logged-in user ("Admin", "Lecturer", or "Student").
     * @return The integer choice selected by the user. Returns 0 for unknown roles.
     */
    public int displayMenu(String role) {
        displaySeparator();
        System.out.println("Welcome, " + role + "!");

        // The logic for *what* menu items exist is handled here, which is an MVC violation.
        // It should be moved to the Controller.
        if (role.equals("Admin")) {
            System.out.println("1. View Timetable (Lecturer/Programme)");
            System.out.println("2. Add New Module");
            System.out.println("3. Add New Room");
            System.out.println("4. Schedule New Session");
            System.out.println("9. Logout");
            System.out.println("0. Exit System");
            return getUserChoice("Enter choice: ");
        } else if (role.equals("Lecturer")) {
            System.out.println("1. View My Timetable");
            System.out.println("9. Logout");
            System.out.println("0. Exit System");
            return getUserChoice("Enter choice: ");
        } else if (role.equals("Student")) {
            System.out.println("1. View My Timetable");
            System.out.println("9. Logout");
            System.out.println("0. Exit System");
            return getUserChoice("Enter choice: ");
        }

        // Default/Unknown user
        return 0;
    }
}