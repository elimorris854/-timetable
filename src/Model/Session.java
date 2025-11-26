package Model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Represents a single instance of a scheduled class (e.g., Lecture, Lab, Tutorial).
 */
public class Session {
    private String sessionID;
    private String moduleCode;
    private String sessionType;
    private String lecturerID;
    private String roomID;
    private List<String> studentGroupIDs;
    private DayOfWeek day;
    private LocalTime startTime;
    private int sessionDuration;

    /**
     * Constructs a scheduled Session, defining all its immutable properties.
     *
     * @param sessionID A unique ID for the session (e.g., "S001").
     * @param moduleCode The module code this session belongs to (e.g., "CS4013").
     * @param sessionType The type of session (e.g., "Lecture", "Lab", "Tutorial").
     * @param lecturerID The ID of the lecturer assigned to the session.
     * @param roomID The ID of the room assigned to the session.
     * @param studentGroupIDs A list of unique IDs for the student groups attending.
     * @param day The day of the week the session occurs.
     * @param startTime The exact time the session begins.
     * @param sessionDuration The length of the session in minutes.
     */
    public Session(String sessionID,String moduleCode,String sessionType,String lecturerID,String roomID, List<String> studentGroupIDs, DayOfWeek day, LocalTime startTime,int sessionDuration){
        this.sessionID=sessionID;
        this.moduleCode = moduleCode;
        this.sessionType = sessionType;
        this.lecturerID = lecturerID;
        this.roomID=roomID;
        this.studentGroupIDs=studentGroupIDs;
        this.day = day;
        this.startTime = startTime;
        this.sessionDuration=sessionDuration;
    }

    /**
     * Gets the unique identifier for this session.
     *
     * @return The session ID string.
     */
    public String getSessionID(){
        return sessionID;
    }

    /**
     * Gets the code of the module associated with this session.
     *
     * @return The module code string.
     */
    public String getModuleCode(){
        return  moduleCode;
    }

    /**
     * Gets the type of the session (e.g., "Lecture", "Lab").
     *
     * @return The session type string.
     */
    public String getSessionType(){
        return sessionType;
    }

    /**
     * Gets the ID of the lecturer assigned to teach this session.
     *
     * @return The lecturer ID string.
     */
    public String getLecturerID(){
        return  lecturerID;
    }

    /**
     * Gets the ID of the room where the session is held.
     *
     * @return The room ID string.
     */
    public String getRoomID(){
        return roomID;
    }

    /**
     * Gets the list of student group IDs attending this session.
     *
     * @return The list of student group IDs.
     */
    public List<String> getStudentGroupIDs(){
        return studentGroupIDs;
    }

    /**
     * Gets the day of the week the session is scheduled on.
     *
     * @return The {@link DayOfWeek} enum value.
     */
    public DayOfWeek getDay(){
        return day;
    }

    /**
     * Gets the start time of the session.
     *
     * @return The {@link LocalTime} object representing the start time.
     */
    public LocalTime getStartTime(){
        return startTime;
    }

    /**
     * Gets the duration of the session in minutes.
     *
     * @return The duration in minutes as an int.
     */
    public int getSessionDuration(){
        return sessionDuration;
    }

    /**
     * Checks if this session temporally overlaps with another session.
     * Assumes sessions are on half-open intervals [start, end).
     *
     * @param other The other {@link Session} to compare against.
     * @return true if the sessions are on the same day and their time ranges intersect, {@code false} otherwise.
     */
    public boolean overLapsWith(Session other){
        if(!this.day.equals(other.day)){
            return false;
        }

        LocalTime thisEndTime= this.startTime.plusMinutes(this.sessionDuration);
        LocalTime otherEndTime=other.startTime.plusMinutes(other.sessionDuration);

        if (thisEndTime.isBefore(other.startTime) || thisEndTime.equals(this.startTime)){
            return false;
        }

        if(otherEndTime.isBefore((this.startTime)) || otherEndTime.equals(this.startTime)){
            return false;
        }
        return true;
    }


}
