package Model;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;

public class Session {
    private String sessionId;
    private String moduleCode;
    private SessionType type;
    private DayOfWeek day;
    private LocalTime startTime;
    private int durationMinutes;
    private String roomId;
    private String lecturerId;
    private List<String> studentGroupIds;

    /**
     * Constructs a new Session object with all required details.
     * @param sessionId A unique identifier for the session.
     * @param moduleCode The code of the module this session belongs to.
     * @param type The type of teaching activity (LECTURE, LAB, TUTORIAL).
     * @param day The day of the week the session is scheduled.
     * @param startTime The time the session begins.
     * @param durationMinutes The length of the session in minutes.
     * @param roomId The ID of the room where the session takes place.
     * @param lecturerId The ID of the lecturer teaching the session.
     * @param studentGroupIds A list of IDs for the student groups attending.
     */
    public Session(String sessionId, String moduleCode, SessionType type, DayOfWeek day, LocalTime startTime, int durationMinutes, String roomId, String lecturerId, List<String> studentGroupIds) {
        this.sessionId = sessionId;
        this.moduleCode = moduleCode;
        this.type = type;
        this.day = day;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.roomId = roomId;
        this.lecturerId = lecturerId;
        this.studentGroupIds = studentGroupIds;
    }

    /**
     * Calculates the end time of the session based on its start time and duration.
     * @return The LocalTime when the session is scheduled to end.
     */
    public LocalTime getEndTime() {
        return this.startTime.plusMinutes(this.durationMinutes);
    }

    /**
     * Determines if this session's time slot conflicts with another session's time slot.
     * The check only considers time, day, duration, and not the resources involved.
     * @param other The other Session object to check against.
     * @return true if the sessions overlap in time on the same day, false otherwise.
     */
    public boolean isOverlap(Session other) {
        if (!this.day.equals(other.day)) {
            return false;
        }

        LocalTime thisEnd = this.getEndTime();
        LocalTime otherEnd = other.getEndTime();

        boolean startsBeforeOtherEnds = this.startTime.isBefore(otherEnd);
        boolean endsAfterOtherStarts = thisEnd.isAfter(other.startTime);

        return startsBeforeOtherEnds && endsAfterOtherStarts;
    }

    public String getSessionId() {
        return sessionId;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public List<String> getStudentGroupIds() {
        return studentGroupIds;
    }

    public SessionType getType() {
        return type;
    }
}
