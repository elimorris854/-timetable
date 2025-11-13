package Model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

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

    public String getSessionID(){
        return sessionID;
    }

    public String getModuleCode(){
        return  moduleCode;
    }

    public String getSessionType(){
        return sessionType;
    }

    public String getLecturerID(){
        return  lecturerID;
    }

    public String getRoomID(){
        return roomID;
    }

    public List<String> getStudentGroupIDs(){
        return studentGroupIDs;
    }

    public DayOfWeek getDay(){
        return day;
    }

    public LocalTime getStartTime(){
        return startTime;
    }

    public int getSessionDuration(){
        return sessionDuration;
    }

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
