package Service;

import Model.*;
import Model.Module;
import Repositories.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for scheduling modules, lectures, labs, and tutorials.
 *
 * <p>This class contains methods for generating sessions while enforcing rules such as:
 * - Students, lecturers, and rooms cannot be double-booked.
 * - Lab sessions must be scheduled in lab rooms.
 * - Session entries must respect module hour requirements.</p>
 */
public class SchedulingService {

    private ModuleRepository moduleRepo;
    private RoomRepository roomRepo;
    private StudentGroupRepository studentGroupRepo;
    private SessionRepository sessionRepo;
    private UserRepository userRepo;

    /**
     * Constructs the SchedulingService with references to all required repositories.
     *
     * @param moduleRepo       repository of modules
     * @param roomRepo         repository of rooms
     * @param studentGroupRepo repository of student groups
     * @param sessionRepo      repository of sessions
     * @param userRepo         repository of users (students/lecturers/admins)
     */
    public SchedulingService(ModuleRepository moduleRepo, RoomRepository roomRepo, StudentGroupRepository studentGroupRepo, SessionRepository sessionRepo, UserRepository userRepo) {
        this.moduleRepo = moduleRepo;
        this.roomRepo = roomRepo;
        this.studentGroupRepo = studentGroupRepo;
        this.sessionRepo = sessionRepo;
        this.userRepo = userRepo;
    }

    private Lecturer getLecturerById(String id) {
        List<User> allUsers = userRepo.getAll();
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            if (user instanceof Lecturer && user.getId().equals(id)) {
                return (Lecturer) user;
            }
        }
        return null;
    }

    private Room getRoomById(String id) {
        List<Room> allRooms = roomRepo.getAll();
        for (int i = 0; i < allRooms.size(); i++) {
            Room room = allRooms.get(i);
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    private StudentGroup getStudentGroupById(String id) {
        List<StudentGroup> allGroups = studentGroupRepo.getAll();
        for (int i = 0; i < allGroups.size(); i++) {
            StudentGroup group = allGroups.get(i);
            if (group.getGroupId().equals(id)) {
                return group;
            }
        }
        return null;
    }

    public String checkSchedulingConstraints(Session newSession) {
        Lecturer lecturer = getLecturerById(newSession.getLecturerID());
        if (lecturer == null) {
            return "Error: Lecturer with ID " + newSession.getLecturerID() + " not found.";
        }

        Room room = getRoomById(newSession.getRoomID());
        if (room == null) {
            return "Error: Room with ID " + newSession.getRoomID() + " not found.";
        }

        List<StudentGroup> attendingGroups = new ArrayList<>();
        int totalGroupSize = 0;
        List<String> studentGroupIDs = newSession.getStudentGroupIDs();

        for (int i = 0; i < studentGroupIDs.size(); i++) {
            String groupId = studentGroupIDs.get(i);
            StudentGroup group = getStudentGroupById(groupId);

            if (group == null) {
                return "Error: Student Group with ID " + groupId + " not found.";
            }
            attendingGroups.add(group);
            totalGroupSize += group.size();
        }

        if (room.getCapacity() < totalGroupSize) {
            return String.format("Room capacity conflict. Room '%s' capacity (%d) is less than total student size (%d).",
                    room.getId(), room.getCapacity(), totalGroupSize);
        }

        if ("Lab".equalsIgnoreCase(newSession.getSessionType()) && room.getType() != Room.RoomType.LAB) {
            return String.format("Room type conflict. Session type is 'Lab' but Room '%s' is a %s.",
                    room.getId(), room.getType().name());
        }

        if (lecturer.getTimetable().checkConflict(newSession)) {
            return String.format("Lecturer '%s' is already booked for another session at this time.", lecturer.getName());
        }

        if (room.getTimetable().checkConflict(newSession)) {
            return String.format("Room '%s' is already booked for another session at this time.", room.getId());
        }

        for (int i = 0; i < attendingGroups.size(); i++) {
            StudentGroup group = attendingGroups.get(i);
            if (group.getTimetable().checkConflict(newSession)) {
                return String.format("Student Group '%s' is already booked for another session at this time.", group.getGroupId());
            }
        }

        return null; // All checks passed.
    }

    public boolean addSession(Session session) throws IllegalStateException {
        String conflictMessage = checkSchedulingConstraints(session);

        if (conflictMessage != null) {
            throw new IllegalStateException("Scheduling Failed: " + conflictMessage);
        }

        // --- Schedule the Session (Only if VALID) ---

        // 1. Update Lecturer's Timetable
        Lecturer lecturer = getLecturerById(session.getLecturerID());
        lecturer.getTimetable().addSession(session);

        // 2. Update Room's Timetable
        Room room = getRoomById(session.getRoomID());
        room.getTimetable().addSession(session);

        // 3. Update Student Groups' Timetables
        List<String> studentGroupIDs = session.getStudentGroupIDs();
        // Traditional index-based for loop to update all group timetables:
        for (int i = 0; i < studentGroupIDs.size(); i++) {
            String groupId = studentGroupIDs.get(i);
            StudentGroup group = getStudentGroupById(groupId);
            // The check in checkSchedulingConstraints ensures 'group' is not null,
            // but a quick defensive check is still good practice.
            if (group != null) {
                group.getTimetable().addSession(session);
            }
        }

        // 4. Persist the Session in the main Session Repository
        sessionRepo.add(session);

        return true;
    }


    /**
     * Removes a session from the system and all associated timetables.
     *
     * @param session The Session object to remove
     */
    public void removeSession(Session session) {

        // Remove from Lecturer's Timetable
        Lecturer lecturer = getLecturerById(session.getLecturerID());
        if (lecturer != null) {
            lecturer.getTimetable().removeSession(session.getSessionID());
        }

        // Remove from Room's Timetable
        Room room = getRoomById(session.getRoomID());
        if (room != null) {
            room.getTimetable().removeSession(session.getSessionID());
        }

        // Remove from Student Groups' Timetables
        List<String> studentGroupIDs = session.getStudentGroupIDs();
        // Traditional index-based for loop to remove the session from all group timetables:
        for (int i = 0; i < studentGroupIDs.size(); i++) {
            String groupId = studentGroupIDs.get(i);
            StudentGroup group = getStudentGroupById(groupId);
            if (group != null) {
                group.getTimetable().removeSession(session.getSessionID());
            }
        }

        // Remove from the main Session Repository
        sessionRepo.remove(session);
    }
}





