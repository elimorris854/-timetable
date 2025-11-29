package Service;

import Model.*;
import Repositories.*;
import java.util.List;

/**
 * Service class responsible for scheduling modules, lectures, labs, and tutorials.
 */
public class SchedulingService {

    private RoomRepository roomRepo;
    private StudentGroupRepository groupRepo;
    private SessionRepository sessionRepo;
    private UserRepository userRepo;

    /**
     * Constructs the SchedulingService with references to all required repositories.
     *
     * @param roomRepo         repository of rooms
     * @param groupRepo of student groups
     * @param sessionRepo      repository of sessions
     * @param userRepo         repository of users (students/lecturers/admins)
     */
    public SchedulingService(RoomRepository roomRepo, StudentGroupRepository groupRepo, SessionRepository sessionRepo, UserRepository userRepo) {
        this.roomRepo = roomRepo;
        this.groupRepo = groupRepo;
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
    }

    /**
     * Checks for sessions clashing
     * @param newSession
     * @return true or false depending if if it clashes
     */
    public boolean scheduleSession(Session newSession) {
        // 1. Validate Room
        Room room = roomRepo.getById(newSession.getRoomID());
        if (room == null) {
            System.out.println("Error: Room " + newSession.getRoomID() + " not found.");
            return false;
        }
        // Check Room Conflict
        if (room.getTimetable().checkConflict(newSession)) {
            System.out.println("Conflict: Room " + newSession.getRoomID() + " is occupied.");
            return false;
        }

        // 2. Validate Lecturer
        User user = userRepo.getById(newSession.getLecturerID());
        if (user == null || !(user instanceof Lecturer)) {
            System.out.println("Error: Invalid Lecturer ID " + newSession.getLecturerID());
            return false;
        }
        Lecturer lecturer = (Lecturer) user;
        // Check Lecturer Conflict
        if (lecturer.getTimetable().checkConflict(newSession)) {
            System.out.println("Conflict: Lecturer " + newSession.getLecturerID() + " is busy.");
            return false;
        }

        // 3. Validate Student Groups
        for (String groupID : newSession.getStudentGroupIDs()) {
            StudentGroup group = groupRepo.getById(groupID);
            if (group == null) {
                System.out.println("Error: Student Group " + groupID + " not found.");
                return false;
            }
            if (group.getTimetable().checkConflict(newSession)) {
                System.out.println("Conflict: Group " + groupID + " has a clash.");
                return false;
            }
        }

        // 4. No conflicts found - Commit the session to all timetables
        room.getTimetable().addSession(newSession);
        lecturer.getTimetable().addSession(newSession);

        for (String groupID : newSession.getStudentGroupIDs()) {
            StudentGroup group = groupRepo.getById(groupID);
            group.getTimetable().addSession(newSession);
        }

        // 5. Save the session itself
        sessionRepo.add(newSession);
        return true;
    }
}