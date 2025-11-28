package Controller;

/**
 * Main controller of the system.
 */
public class AppController {

    public AppController(View.CLIView view) {
    }

    /**
     * Performs login for a user.
     */
    public boolean login(String email, String password) { return false; }

    /**
     * Retrieves timetable for a student.
     */
    public String getStudentTimetable(String studentId) { return null; }

    /**
     * Retrieves timetable for a lecturer.
     */
    public String getLecturerTimetable(String lecturerId) { return null; }

    /**
     * Retrieves timetable for a programme.
     */
    public String getProgrammeTimetable(String programmeCode) { return null; }

    /**
     * Adds a new module (admin operation).
     */
    public void addModule(String code, String name, int lecHrs, int labHrs, int tutHrs) {}

    /**
     * Adds a new room (admin operation).
     */
    public void addRoom(String roomId, int capacity, boolean isLab) {}
}
