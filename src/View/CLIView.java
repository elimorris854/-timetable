package View;

import Model.Session;

import java.util.Scanner;
import java.util.Map;
import java.util.List;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.time.LocalTime;

/**
 * Command-line interface view for the timetabling system.
 * This class handles all user input and output, conforming to the MVC View pattern.
 */
public class CLIView {

    private final Scanner scanner;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

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
     * Displays an error message to the user.
     */
    public void displayError(String message) {
        System.err.println("\n*** ERROR: " + message + " ***");
    }

    /**
     * Displays the welcome screen and returns the user's choice (1 for Login, 0 for Exit).
     * @return The integer option selected by the user.
     */
    public int displayLoginMenu() {
        System.out.println("\n**************************************************");
        System.out.println("* University of Limerick Timetabling Application *");
        System.out.println("**************************************************");
        System.out.println("1. Login");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
        return getIntInput();
    }

    /**
     * Displays a menu and captures user input.
     * @param isAdmin True if the current user is an admin.
     * @return The integer option selected by the user.
     */
    public int displayMenu(boolean isAdmin) {
        displayMessage("Main Menu");
        System.out.println("1. View My Timetable");
        System.out.println("2. Query Timetable (Lecturer/Group/Room)");
        if (isAdmin) {
            System.out.println("3. Admin: Data Management (Load/Save/View All)");
            System.out.println("4. Admin: User/Module/Room Management (CRUD)");
            System.out.println("5. Admin: Generate Timetable (Auto-Schedule)");
        }
        System.out.println("0. Logout / Exit");
        System.out.print("Enter option: ");
        return getIntInput();
    }

    /**
     * Displays the Admin Data Management Menu.
     * @return The integer option selected by the user.
     */
    public int displayAdminDataMenu() {
        displayMessage("Admin Data Management");
        System.out.println("1. Load All Data from CSV Files");
        System.out.println("2. Save All Data to CSV Files");
        System.out.println("3. View Data Summary");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter option: ");
        return getIntInput();
    }

    /**
     * Displays the Timetable Query Menu.
     * @return The integer option selected by the user.
     */
    public int displayQueryMenu() {
        displayMessage("Timetable Query");
        System.out.println("1. Query Lecturer Timetable");
        System.out.println("2. Query Student Group Timetable");
        System.out.println("3. Query Room Timetable");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter option: ");
        return getIntInput();
    }

    /**
     * Requests login credentials from the user.
     * @return A String array containing {userId, password}.
     */
    public String[] requestLogin() {
        displayMessage("Login Required");
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        return new String[]{userId, password};
    }

    /**
     * Requests the specific item ID (e.g., Lecturer ID, Group Code, Room ID)
     * the user wants to view the schedule for.
     * @param type The type of item being requested (e.g., "Lecturer ID").
     * @return The user's input string.
     */
    public String requestViewDetails(String type) {
        System.out.print("Enter the " + type + " to view: ");
        return scanner.nextLine();
    }

    /**
     * Requests confirmation for a critical action, such as scheduling or saving data.
     * @param action The action being confirmed (e.g., "reschedule all modules").
     * @return True if the user enters 'yes', false otherwise.
     */
    public boolean requestConfirmation(String action) {
        System.out.print("\nWARNING: Are you sure you want to " + action + "? (yes/no): ");
        return scanner.nextLine().trim().equalsIgnoreCase("yes");
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

    /**
     * Requests a comma-separated list of IDs (e.g., Lecturer IDs, Group Codes) from the user.
     * @param type The type of ID being requested (e.g., "Lecturer IDs").
     * @return A List of trimmed String IDs.
     */
    public List<String> requestListOfIds(String type) {
        String input = requestString("Enter comma-separated list of " + type);
        return Arrays.asList(input.split(",")).stream()
                .map(String::trim)
                .toList();
    }

    /**
     * Requests all necessary data fields to create a new Module object.
     * @return A Map containing the raw string data for the Module's fields.
     */
    public Map<String, String> requestNewModuleData() {
        Map<String, String> data = new HashMap<>();
        displayMessage("Enter New Module Details");

        data.put("code", requestString("Module Code (e.g., CS4013)"));
        data.put("name", requestString("Module Name"));

        /* Hour fields need integer input */
        System.out.print("Lecture Hours per week: ");
        data.put("lectureHours", String.valueOf(getIntInput()));

        System.out.print("Lab Hours per week: ");
        data.put("labHours", String.valueOf(getIntInput()));

        System.out.print("Tutorial Hours per week: ");
        data.put("tutorialHours", String.valueOf(getIntInput()));

        /* Lecturers assigned */
        data.put("lecturerIds", requestListOfIds("Lecturer IDs (e.g., L12345678, L87654321)").toString());

        return data;
    }

    /**
     * A generic method to display a timetable map (DayOfWeek to List of Sessions).
     * @param title The title for the timetable display.
     * @param timetableMap The map containing DayOfWeek and its list of scheduled Sessions.
     */
    public void displayTimetable(String title, Map<DayOfWeek, List<Session>> timetableMap) {
        System.out.println("\n=========================================================================================================================");
        System.out.println("                                                            " + title);
        System.out.println("=========================================================================================================================");

        boolean foundSession = false;

        /* Iterate through all days of the week in order, starting with Monday */
        for (DayOfWeek day : DayOfWeek.values()) {
            List<Session> sessions = timetableMap.getOrDefault(day, Collections.emptyList());

            if (!sessions.isEmpty()) {
                foundSession = true;

                System.out.println("\n--- " + day.toString() + " ---");

                /* Sort sessions by start time for clean display */
                sessions.sort((s1, s2) -> s1.getStartTime().compareTo(s2.getStartTime()));

                System.out.printf("%-10s %-10s %-10s %-10s %-15s %-15s %-40s%n",
                        "Start", "End", "Type", "Module", "Lecturer", "Room", "Student Group(s)");
                System.out.println("-------------------------------------------------------------------------------------------------------------------------");

                for (Session session : sessions) {
                    /* Calculate the end time using the user's LocalTime.plusMinutes() method */
                    LocalTime endTime = session.getStartTime().plusMinutes(session.getSessionDuration());

                    /* Displaying the list of student groups as a comma-separated string */
                    String groupList = String.join(", ", session.getStudentGroupIDs());

                    System.out.printf("%-10s %-10s %-10s %-10s %-15s %-15s %-40s%n",
                            session.getStartTime().format(timeFormatter),
                            endTime.format(timeFormatter),
                            session.getSessionType(),
                            session.getModuleCode(),
                            session.getLecturerID(),
                            session.getRoomID(),
                            groupList);
                }
            }
        }

        if (!foundSession) {
            System.out.println("No sessions scheduled for this timetable.");
        }

        System.out.println("\n=========================================================================================================================");
    }

    /**
     * Displays the application's data summary.
     * @param summary A string containing the counts of all data objects.
     */
    public void displayDataSummary(String summary) {
        displayMessage("Data Summary");
        System.out.println(summary);
    }

    /**
     * Pauses the CLI until the user presses Enter.
     */
    public void pressEnterToContinue() {
        System.out.print("\nPress Enter to return to the menu...");
        try {
            scanner.nextLine();
        } catch (Exception e) {
            /* Ignore exception, user just pressed enter */
        }
    }
}