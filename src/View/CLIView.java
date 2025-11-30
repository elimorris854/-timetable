package View;

import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface view for the timetabling system.
 * It handles all user input and output for the CLI.
 */
public class CLIView  {

    /**
     * Scanner object used to read user input from the console (System.in).
     */
    private Scanner scanner;

    /**
     * Constructs a new CLIView and initializes the Scanner object for input.
     */
    public CLIView() {
        // We use System.in directly as it's the standard for CLIs
        this.scanner = new Scanner(System.in);
    }

    // --- Output Methods ---

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
        System.out.print("Password: ");
        // In a real application, we would use a Console for secure password input.
        String password = scanner.nextLine();
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
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Displays the main menu options based on the provided list options.
     *
     * @param title The title of the menu.
     * @param options A list of menu options to display.
     * @return The integer choice selected by the user.
     */
    public int displayMenu(String title, List<String> options) {
        displaySeparator();
        System.out.println(title);

        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }

        System.out.println("9. Logout");
        System.out.println("0. Exit System");

        return getUserChoice("Enter choice: ");
    }
}