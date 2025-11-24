package View;

import java.util.Scanner;

/**
 * Command-line interface view for the timetabling system.
 * This class handles all user input and output, conforming to the MVC View pattern.
 */
public class CLIView {

    private final Scanner scanner;

    public CLIView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays a general message to the user.
     */
    public void displayMessage(String message) {
        System.out.println("\n--- " + message + " ---");
    }

    /**
     * Displays a timetable string to the user.
     */
    public void displayTimetable(String timetable) {
        displayMessage("Schedule");
        System.out.println(timetable);
    }

    /**
     * Displays a menu and captures user input.
     * @param isAdmin True if the current user is an admin.
     * @return The integer option selected by the user.
     */
    public int displayMenu(boolean isAdmin) {
        displayMessage("Main Menu");
        System.out.println("1. View My Timetable");
        System.out.println("2. View Programme/Module/Room Schedule");
        if (isAdmin) {
            System.out.println("3. Admin Functions (Manual Change)");
            System.out.println("4. Add New Module");
            System.out.println("5. Add New Room");
            System.out.println("6. Schedule All Modules (Auto)");
        }
        System.out.println("0. Logout / Exit");
        System.out.print("Enter option: ");
        return getIntInput();
    }

    /**
     * Requests login credentials from the user.
     * @return A String array containing {username, password}.
     */
    public String[] requestLogin() {
        displayMessage("Login Required");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        return new String[]{email, password};
    }

    /**
     * Requests the specific item (e.g., Programme Code, Module Code, Room ID)
     * the user wants to view the schedule for.
     * @param type The type of item being requested (e.g., "Programme Code").
     * @return The user's input string.
     */
    public String requestViewDetails(String type) {
        System.out.print("Enter the " + type + " to view: ");
        return scanner.nextLine();
    }

    /**
     * Requests a generic string input from the user with a custom prompt.
     * @param prompt The prompt to display to the user.
     * @return The user's input string.
     */
    public String requestString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    /**
     * Helper to safely read an integer from the user.
     */
    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print("Enter option: ");
        }
        int input = scanner.nextInt();
        scanner.nextLine();
        return input;
    }
}