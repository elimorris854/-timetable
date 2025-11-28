package Controller;
import Model.*;
import Repositories.*;
import Service.*;
import View.*; // Import the view
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

    public AppController(View.CLIView view) {
        // 1. Initialize all repositories
        this.userRepo = new UserRepository();
        this.roomRepo = new RoomRepository();
        this.moduleRepo = new ModuleRepository();
        this.sessionRepo = new SessionRepository();
        this.groupRepo = new StudentGroupRepository();
        this.programmeRepo = new ProgrammeRepository();

        // 2. Initialize the service and view
        this.schedulingService = new SchedulingService(roomRepo, userRepo, groupRepo, sessionRepo);
        this.cliView = new CLIView();

        // 3. Load initial data
        seedData();
    }

    public AppController(View.CLIView view) {
    }

    // NOTE: This method is private and used only for initial setup.


    // ************************************************************
    // * NEW RUN METHOD & MENU LOGIC                *
    // ************************************************************

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
                    cliView.displayMessage(" Login Successful. Role: " + currentUser.getRole());
                } else {
                    cliView.displayMessage(" Login failed. Invalid email or password.");
                }
            }

            // 2. Main Menu Loop (after successful login)
            int choice;
            String role = currentUser.getRole();

            do {
                choice = cliView.displayMenu(role);

                switch (role) {
                    case "Admin":
                        running = handleAdminMenu(choice);
                        break;
                    case "Lecturer":
                        running = handleLecturerMenu(choice);
                        break;
                    case "Student":
                        running = handleStudentMenu(choice);
                        break;
                    default:
                        // Logout and reset if role is unknown
                        currentUser = null;
                        running = true;
                        break;
                }
            } while (running && currentUser != null && choice != 0 && choice != 9);

            // Handle logout choice (choice 9)
            if (choice == 9) {
                currentUser = null;
            }
            // If running is false (choice 0), the outer loop terminates.
        }
        cliView.displayMessage("System Shutting Down. Goodbye!");
        cliView.displaySeparator();
    }

    // ************************************************************
    // * MENU HANDLERS                           *
    // ************************************************************

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

    private boolean handleAdminMenu(int choice) {
        switch (choice) {
            case 1: // View Timetable (Lec/Prog)
                handleTimetableView();
                return true;
            case 2: // Add New Module
                // The implementation for this is complex and is kept simple for now
                cliView.displayMessage("Functionality: Add New Module - Pending full implementation.");
                return true;
            case 3: // Add New Room
                // The implementation for this is complex and is kept simple for now
                cliView.displayMessage("Functionality: Add New Room - Pending full implementation.");
                return true;
            case 4: // Schedule New Session
                handleScheduleSession();
                return true;
            case 9: // Logout
                return true;
            case 0: // Exit System
                return false;
            default:
                cliView.displayMessage("Invalid choice. Please try again.");
                return true;
        }
    }

    private boolean handleLecturerMenu(int choice) {
        switch (choice) {
            case 1: // View My Timetable
                String timetable = getLecturerTimetable(currentUser.getId());
                cliView.displayTimetable(timetable);
                return true;
            case 9: // Logout
                return true;
            case 0: // Exit System
                return false;
            default:
                cliView.displayMessage("Invalid choice. Please try again.");
                return true;
        }
    }

    private boolean handleStudentMenu(int choice) {
        switch (choice) {
            case 1: // View My Timetable
                String timetable = getStudentTimetable(currentUser.getId());
                cliView.displayTimetable(timetable);
                return true;
            case 9: // Logout
                return true;
            case 0: // Exit System
                return false;
            default:
                cliView.displayMessage("Invalid choice. Please try again.");
                return true;
        }
    }

    // ************************************************************
    // * HELPER METHODS (Input/Output)              *
    // ************************************************************

    private void handleTimetableView() {
        cliView.displayMessage("View Timetable Options:");
        cliView.displayMessage("1. Lecturer Timetable (Enter ID)");
        cliView.displayMessage("2. Programme Timetable (Enter Code)");
        int choice = cliView.getUserChoice("Enter choice (1 or 2): ");

        String targetID;
        String timetable = "Timetable not found.";

        if (choice == 1) {
            targetID = cliView.requestInput("Enter Lecturer ID (e.g., L001): ");
            timetable = getLecturerTimetable(targetID);
        } else if (choice == 2) {
            targetID = cliView.requestInput("Enter Programme Code (e.g., CS): ");
            timetable = getProgrammeTimetable(targetID);
        } else {
            cliView.displayMessage("Invalid selection.");
            return;
        }

        cliView.displayTimetable(timetable);
    }

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
                cliView.displayMessage("✅ Session " + id + " scheduled successfully.");
            } else {
                cliView.displayMessage("❌ Scheduling failed. A conflict was found. Check details above.");
            }

        } catch (IllegalArgumentException e) {
            cliView.displayMessage("❌ Input Error: " + e.getMessage());
        } catch (Exception e) {
            cliView.displayMessage("❌ An unexpected error occurred: " + e.getMessage());
        }
    }


    // ************************************************************
    // * EXISTING BUSINESS LOGIC                    *
    // ************************************************************

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

    public User getCurrentUser() {
        return currentUser;
    }

    /** Retrieves timetable for a student. */
    public String getStudentTimetable(String studentId) {
        User u = userRepo.getById(studentId);
        if (u instanceof Student) {
            // In a real application, student's timetable is their group's timetable
            Student student = (Student) u;
            return student.getStudentGroup().getTimetable().toString(); // Use the Group's timetable
        }
        return "Student not found or invalid ID.";
    }

    /** Retrieves timetable for a lecturer. */
    public String getLecturerTimetable(String lecturerId) {
        User u = userRepo.getById(lecturerId);

        if (u != null && u instanceof Lecturer) {
            Lecturer lec = (Lecturer) u;
            List<Session> sessions = lec.getTimetable().getSchedulesSessions();

            if (sessions.isEmpty()) {
                return "No sessions scheduled for " + lec.getName();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Timetable for ").append(lec.getName()).append(":\n");
            for (Session s : sessions) {
                sb.append("- ").append(s.getDay())
                        .append(" at ").append(s.getStartTime())
                        .append(": ").append(s.getModuleCode())
                        .append(" (").append(s.getRoomID()).append(")\n");
            }
            return sb.toString();
        }
        return "Lecturer not found or invalid ID.";
    }

    public String getProgrammeTimetable(String programmeCode) {
        return "Programme timetable logic pending.";
    }

    public void addModule(String code, String name, int lecHrs, int labHrs, int tutHrs) {
        if (currentUser instanceof Admin) {
            List<String> lecs = new ArrayList<>();
            Module m = new Module(code, name, lecHrs, labHrs, tutHrs, lecs);
            moduleRepo.add(m);
        }
    }

    public void addRoom(String roomId, int capacity, boolean isLab) {
        if (currentUser instanceof Admin) {
            Room.RoomType type = isLab ? Room.RoomType.LAB : Room.RoomType.CLASSROOM;
            roomRepo.add(new Room(roomId, capacity, type));
        }
    }

    public boolean scheduleSession(Session session) {
        if (currentUser instanceof Admin) {
            return schedulingService.scheduleSession(session);
        }
        return false;
    }
}