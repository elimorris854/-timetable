package Controller;
import Model.*;
import Model.Session;
import Model.Student;
import Model.User;
import Repositories.*;
import Model.Module;
import Service.SchedulingService;
import View.CLIView; // Import the view
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.DayOfWeek;

/**
 * Main controller of the system.
 */
public class AppController {

    private UserRepository userRepo;
    private RoomRepository roomRepo;
    private ModuleRepository moduleRepo;
    private SessionRepository sessionRepo;
    private StudentGroupRepository groupRepo;
    private ProgrammeRepository programmeRepo;

    private SchedulingService schedulingService;
    private CLIView cliView; // The CLI for input/output

    private User currentUser;

    /**
     * Constructor for AppController, initialises all repositories and schedulingService and CLIView.
     */
    public AppController() {
        // 1. Initialize all repositories
        this.userRepo = new UserRepository();
        this.roomRepo = new RoomRepository();
        this.moduleRepo = new ModuleRepository();
        this.sessionRepo = new SessionRepository();
        this.groupRepo = new StudentGroupRepository();
        this.programmeRepo = new ProgrammeRepository();

        // 2. Initialize the service and view
        this.schedulingService = new SchedulingService(roomRepo, groupRepo, sessionRepo, userRepo);
        this.cliView = new CLIView();

        // 3. Load initial data
        seedData();
    }

    /**
     * The main execution loop for the application, handling login and menu navigation.
     */
    public void run() {
        boolean running = true;
        while (running) {
            // 1. Login Loop
            while (currentUser == null) {
                String[] credentials = cliView.requestLogin();
                if (login(credentials[0], credentials[1])) {
                    cliView.displayMessage("Login Successful. Role: " + currentUser.getRole());
                } else {
                    cliView.displayMessage("Login failed. Invalid email or password.");
                }
            }

            // 2. Main Menu Loop (after successful login)
            int choice = 0;
            String role = currentUser.getRole();

            // Define menu options here (MVC Compliance)
            List<String> menuOptions = new ArrayList<>();
            if (role.equals("Admin")) {
                menuOptions.add("View Timetable (Lecturer/Programme/Student)");
                menuOptions.add("Add New Module");
                menuOptions.add("Add New Room");
                menuOptions.add("Schedule New Session");
                menuOptions.add("Save to CSVs");
                // menuOptions.add("Cancel Session"); // Feature reserved for teammate
            } else if (role.equals("Lecturer") || role.equals("Student")) {
                menuOptions.add("View My Timetable");
            }

            do {
                // Pass the list of options to the view
                choice = cliView.displayMenu("Welcome, " + role + "!", menuOptions);

                // Handle Logout and Exit first
                if (choice == 9) {
                    currentUser = null;
                    break;
                }
                if (choice == 0) {
                    running = false;
                    break;
                }

                switch (role) {
                    case "Admin":
                        handleAdminMenu(choice);
                        break;
                    case "Lecturer":
                        handleLecturerMenu(choice);
                        break;
                    case "Student":
                        handleStudentMenu(choice);
                        break;
                    default:
                        // Logout and reset if role is unknown
                        currentUser = null;
                        break;
                }
            } while (running && currentUser != null);
        }
        cliView.displayMessage("System Shutting Down. Goodbye!");
        cliView.displaySeparator();
    }

    /**
     * Seeds test data from CSVs using repositories.
     */
    private void seedData() {
        System.out.println("Loading system data from CSV files...");

        // 1. Load basic entities (no external dependencies)
        // These methods will need the proper java.io.* imports in their classes
        programmeRepo.loadData();
        moduleRepo.loadData();
        roomRepo.loadData();

        // 2. Load Student Groups (Requires Programme data)
        groupRepo.loadData(programmeRepo);

        // 3. Load Users (Requires StudentGroup data)
        userRepo.loadData(groupRepo);

        // 4. Load Sessions (Requires ALL repositories)
        sessionRepo.loadData(userRepo, roomRepo, groupRepo, moduleRepo);

        System.out.println("Data loading complete. System ready.");
    }

    /**
     * Handles the admin menu, takes in a user's choice and calls the corresponding action.
     * @param choice the user's choice from the menu.
     * @return true unless the choice is Exit, then false.
     */
    private void handleAdminMenu(int choice) {
        switch (choice) {
            case 1: // View Timetable (Student/Lec/Prog)
                handleTimetableView();
                break;
            case 2: // Add New Module
                handleAddModule();
                break;
            case 3: // Add New Room
                handleAddRoom();
                break;
            case 4: // Schedule New Session
                handleScheduleSession();
                break;
            case 5: // Save to CSVs
                handleSaveCSVs();
                break;
            default:
                cliView.displayMessage("Invalid choice. Please try again.");
                break;
        }
    }

    /**
     * Handles the lecturer menu, takes in a user's choice and calls the corresponding action.
     * @param choice the user's choice from the menu.
     * @return true unless the choice is Exit, then false.
     */
    private void handleLecturerMenu(int choice) {
        switch (choice) {
            case 1: // View My Timetable
                String timetable = getLecturerTimetable(currentUser.getId());
                cliView.displayTimetable(timetable);
                break;
            default:
                cliView.displayMessage("Invalid choice. Please try again.");
                break;
        }
    }

    /**
     * Handles the student menu, takes in a user's choice and calls the corresponding action.
     * @param choice the user's choice from the menu.
     * @return true unless the choice is Exit, then false.
     */
    private void handleStudentMenu(int choice) {
        switch (choice) {
            case 1: // View My Timetable
                String timetable = getStudentTimetable(currentUser.getId());
                cliView.displayTimetable(timetable);
                break;
            default:
                cliView.displayMessage("Invalid choice. Please try again.");
                break;
        }
    }

    /**
     * Handles the timetable viewing function for admins.
     */
    private void handleTimetableView() {
        cliView.displayMessage("View Timetable Options:");
        cliView.displayMessage("1. Lecturer Timetable (Enter ID)");
        cliView.displayMessage("2. Programme Timetable (Enter Code)");
        cliView.displayMessage("3. Student Timetable (Enter ID)");
        int choice = cliView.getUserChoice("Enter choice (1 or 2 or 3): ");

        String targetID;
        String timetable = "Timetable not found.";

        if (choice == 1) {
            targetID = cliView.requestInput("Enter Lecturer ID (e.g., L001): ");
            timetable = getLecturerTimetable(targetID);
        } else if (choice == 2) {
            targetID = cliView.requestInput("Enter Programme Code (e.g., CS): ");
            timetable = getProgrammeTimetable(targetID);
        } else if (choice == 3) {
            targetID = cliView.requestInput("Enter Student ID (e.g., S1001): ");
            timetable = getStudentTimetable(targetID);
        } else {
            cliView.displayMessage("Invalid selection.");
            return;
        }

        cliView.displayTimetable(timetable);
    }

    /**
     * Handles the schedule session option for admins.
     */
    private void handleScheduleSession() {
        cliView.displayMessage("--- Schedule New Session ---");

        try {
            // Collect all required data for a Session object
            String id = cliView.requestInput("Session ID (e.g., S001): ");
            String moduleCode = cliView.requestInput("Module Code (e.g., CS4013): ");
            String sessionType = cliView.requestInput("Session Type (e.g., Lecture, Lab): ");
            String lecturerID = cliView.requestInput("Lecturer ID (e.g., L001): ");
            String roomID = cliView.requestInput("Room ID (e.g., KB1-11): ");

            // Simple group handling (comma-separated list)
            String groupsStr = cliView.requestInput("Student Group IDs (comma-separated, e.g., CS1A,CS1B): ");
            List<String> studentGroupIDs = List.of(groupsStr.split(","));

            String dayStr = cliView.requestInput("Day of Week (e.g., MONDAY): ").toUpperCase();
            DayOfWeek day = DayOfWeek.valueOf(dayStr);

            String timeStr = cliView.requestInput("Start Time (HH:mm, e.g., 10:00): ");
            LocalTime startTime = LocalTime.parse(timeStr);

            int duration = cliView.getUserChoice("Duration in minutes (e.g., 60, 120): ");

            // Create the session
            Session newSession = new Session(id, moduleCode, sessionType, lecturerID, roomID,
                    studentGroupIDs, day, startTime, duration);

            // Call the scheduling logic
            if (scheduleSession(newSession)) {
                cliView.displayMessage("Session " + id + " scheduled successfully.");
            } else {
                cliView.displayMessage("Scheduling failed. A conflict was found. Check details above.");
            }

        } catch (IllegalArgumentException e) {
            cliView.displayMessage("Input Error: " + e.getMessage());
        } catch (Exception e) {
            cliView.displayMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Handles the saving of all current timetable data to new output CSVs.
     */
    private void handleSaveCSVs() {
        userRepo.saveData();
        moduleRepo.saveData();
        roomRepo.saveData();
        sessionRepo.saveData();
        groupRepo.saveData();
        programmeRepo.saveData();
    }

    /** Performs login for a user. */
    public boolean login(String email, String password) {
        User user = userRepo.getByEmail(email);
        // NOTE: This requires the checkPassword method in User.java
        if (user != null && user.checkPassword(password)) {
            this.currentUser = user;
            return true;
        }
        return false;
    }

    /** Retrieves timetable for a student.
     * @param studentId the ID of the student.
     * @return the timetable for that student.
     */
    public String getStudentTimetable(String studentId) {
        User u = userRepo.getById(studentId);
        if (u instanceof Student) {
            // In a real application, student's timetable is their group's timetable
            Student student = (Student) u;
            return student.getStudentGroup().getTimetable().toString(); // Use the Group's timetable
        }
        return "Student not found or invalid ID.";
    }

    /** Retrieves timetable for a lecturer.
     * @param lecturerId the lecturer ID.
     * @return the timetable for the lecturer.
     */
    public String getLecturerTimetable(String lecturerId) {
        User u = userRepo.getById(lecturerId);

        if (u instanceof Lecturer) {
            Lecturer lec = (Lecturer) u;

            String timetableString = lec.getTimetable().toString();

            return "Timetable for " + lec.getName() + ":\n" + timetableString;
        }

        return "Lecturer not found or invalid ID.";
    }

    /**
     * Gets timetable for programme (logic is not yet implemented).
     * @param programmeCode the code for the programme.
     * @return the timetable in String form.
     */
    public String getProgrammeTimetable(String programmeCode) {
        return "Programme timetable logic not yet implemented.";
    }

    /** Handles adding a new Module by Admin */
    private void handleAddModule() {
        if (!(currentUser instanceof Admin)) {
            cliView.displayMessage("Only Admin can add modules.");
            return;
        }

        try {
            cliView.displayMessage("--- Add New Module ---");

            String code = cliView.requestInput("Module Code (e.g., CS4013): ").toUpperCase();
            String name = cliView.requestInput("Module Name: ");
            int lecHrs = cliView.getUserChoice("Lecture Hours (integer): ");
            int labHrs = cliView.getUserChoice("Lab Hours (integer): ");
            int tutHrs = cliView.getUserChoice("Tutorial Hours (integer): ");

            // Optional: Ask for lecturer IDs (comma-separated)
            String lecturersStr = cliView.requestInput("Lecturer IDs (comma-separated, optional): ");
            List<String> lecturerIds = lecturersStr.isEmpty() ? new ArrayList<>() : List.of(lecturersStr.split(","));

            // Create and add the module
            Module module = new Module(code, name, lecHrs, labHrs, tutHrs, lecturerIds);
            moduleRepo.add(module);

            cliView.displayMessage("Module " + code + " added successfully.");

        } catch (NumberFormatException e) {
            cliView.displayMessage("Invalid number format. Module not added.");
        } catch (Exception e) {
            cliView.displayMessage("Error adding module: " + e.getMessage());
        }
    }

    /** Handles adding a new Room by Admin */
    private void handleAddRoom() {
        if (!(currentUser instanceof Admin)) {
            cliView.displayMessage("Only Admin can add rooms.");
            return;
        }

        try {
            cliView.displayMessage("--- Add New Room ---");

            String roomId = cliView.requestInput("Room ID (e.g., KB1-11): ").toUpperCase();
            int capacity = cliView.getUserChoice("Room Capacity (integer): ");
            String typeStr = cliView.requestInput("Is this a Lab? (yes/no): ").toLowerCase();
            boolean isLab = typeStr.equals("yes") || typeStr.equals("y");

            Room.RoomType type = isLab ? Room.RoomType.LAB : Room.RoomType.CLASSROOM;
            Room room = new Room(roomId, capacity, type);
            roomRepo.add(room);

            cliView.displayMessage("Room " + roomId + " added successfully.");

        } catch (NumberFormatException e) {
            cliView.displayMessage("Invalid number format. Room not added.");
        } catch (Exception e) {
            cliView.displayMessage("Error adding room: " + e.getMessage());
        }
    }

    /**
     * Schedules a session using the scheduling service.
     * @param session the session to be schduled.
     * @return true if session was successfully scheduled, otherwise false.
     */
    public boolean scheduleSession(Session session) {
        if (currentUser instanceof Admin) {
            return schedulingService.scheduleSession(session);
        }
        return false;
    }
}