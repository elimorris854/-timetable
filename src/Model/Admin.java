package Model;

/**
 * Represents an administrative user with privileges to manage timetables and data.
 */
public class Admin extends User {

    /**
     * Constructor for Admin.
     *
     * @param id unique identifier
     * @param name full name
     * @param email email address
     */
    public Admin(String id, String name, String email,String password){
        super(id, name, email,password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    /**
     * Modify or update a timetable.
     *
     * @param timetable timetable to be updated
     * @param session new session details
     */
    public void modifyTimetable(Timetable timetable, Session session) {
        timetable.addSession(session);
    }

    /**
     * Remove a session from a timetable.
     *
     * @param timetable timetable to be updated
     * @param session session to remove
     */
    public void removeSession(Timetable timetable, Session session) {
        timetable.removeSession(session.getSessionID());
    }
}

