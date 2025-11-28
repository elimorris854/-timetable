package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the schedule for a single owner, such as a Lecturer, Room, or StudentGroup.
 * It manages a collection of sessions and is responsible for checking and preventing time conflicts.
 */
public class Timetable {
    private String ownerID;
    private List<Session> schedulesSessions;

    /**
     * Constructs a new Timetable instance for a specific entity.
     *
     * @param ownerID The unique ID of the entity (Room, Lecturer, or StudentGroup) this timetable is for.
     */
    public Timetable(String ownerID) {
        this.ownerID = ownerID;
        this.schedulesSessions = new ArrayList<>();
    }

    /**
     * Gets the unique ID of the timetable's owner.
     *
     * @return The unique owner ID string.
     */
    public String getOwnerID() {
        return ownerID;
    }

    /**
     * Provides a list of all sessions currently scheduled in this timetable.
     *
     * @return A copy of the list of {@link Session} objects representing the schedule.
     */
    public List<Session> getSchedulesSessions() {
        return new ArrayList<>(schedulesSessions);
    }

    /**
     * Attempts to add a new Session to the timetable.
     * Checks for time conflicts before adding
     *
     * @param newSession The session to be scheduled.
     * @return {@code true} if the session was successfully added (no conflict), {@code false} otherwise.
     */
    public boolean addSession(Session newSession) {
        if (checkConflict(newSession)) {
            return false;
        }
        schedulesSessions.add(newSession);
        return true;
    }

    /**
     * Removes a session from the timetable based on its unique ID.
     *
     * @param sessionId The unique ID of the session to remove
     */
    public void removeSession(String sessionId) {
        for (int i = 0; i < schedulesSessions.size(); i++) {
            Session existingSession = schedulesSessions.get(i);
            if (existingSession.getSessionID().equals(sessionId)) {
                schedulesSessions.remove(i);
                return;
            }
        }
    }

    /**
     * Checks if a proposed session conflicts with any existing scheduled sessions.
     *
     * @param newSession The session to check for potential conflicts.
     * @return {@code true} if a time conflict is detected, {@code false} otherwise.
     */
    public boolean checkConflict(Session newSession) {
        for (int i = 0; i < schedulesSessions.size(); i++) {
            Session existingSession = schedulesSessions.get(i);
            if (newSession.overLapsWith(existingSession)) {
                return true;
            }
        }
        return false;
    }
    /** t checks if the timetable has any scheduled sessions.

     If empty, it returns the simple message: "No sessions scheduled."*/
    @Override
    public String toString() {
        if (schedulesSessions.isEmpty()) {
            return "No sessions scheduled.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Timetable for ").append(ownerID).append(":\n");

        for (Session s : schedulesSessions) {
            sb.append("- ")
                    .append(s.getDay())
                    .append(" at ").append(s.getStartTime())
                    .append(": ").append(s.getModuleCode())
                    .append(" (").append(s.getRoomID()).append(")\n");
        }

        return sb.toString();
    }
}