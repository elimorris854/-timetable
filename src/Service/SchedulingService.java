package Service;


import Model.Session;
import repository.TimetableRepository;
import java.util.List;

public class SchedulingService {
    private final TimetableRepository timetableRepository;

    /**
     * Constructs the SchedulingService, injecting required data dependencies.
     * @param timetableRepository Repository for accessing resource timetables (Room, Lecturer, StudentGroup).
     */
    public SchedulingService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    /**
     * Checks if a proposed session can be scheduled without double-booking any resources.
     * This method enforces the core rule that students, lecturers, and rooms cannot be double booked.
     * @param session The Session object proposed for scheduling.
     * @return true if the session can be scheduled (no conflicts), false otherwise.
     */
    public boolean checkAvailability(Session session) {

        if (timetableRepository.getTimetable(session.getRoomId()).hasConflict(session)) {
            return false;
        }

        if (timetableRepository.getTimetable(session.getLecturerId()).hasConflict(session)) {
            return false;
        }

        List<String> studentGroupIds = session.getStudentGroupIds();
        for (String groupId : studentGroupIds) {
            if (timetableRepository.getTimetable(groupId).hasConflict(session)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Adds a validated session to the timetables of all associated resources.
     * This method assumes checkAvailability has been called and passed.
     * @param session The Session object to be scheduled.
     */
    public void scheduleSession(Session session) {
        // Implementation logic would typically involve calling methods on the repositories
        // to retrieve, update, and save the modified Timetable objects for the Room, Lecturer, and Student Groups.
        // For a full implementation, you would need update/save methods in TimetableRepository.
    }
}