package Model;

import java.util.ArrayList;
import java.util.List;

public class Timetable{
    private String ownerID;
    private List<Session> schedulesSessions;

    public Timetable(String ownerID){
        this.ownerID = ownerID;
        this.schedulesSessions = new ArrayList<>();
    }

    public String getOwnerID(){
        return ownerID;
    }

    public List<Session> getSchedulesSessions(){
        return new ArrayList<>(schedulesSessions);
    }

    public boolean addSession(Session newSession){
        if(checkConflict(newSession)){
            return false;
        }
        schedulesSessions.add(newSession);
        return true;
    }

    public void removeSession(String sessionId){
    }

    public boolean checkConflict(Session newSession){
        for(int i=0;i<schedulesSessions.size();i++){
            Session existingSession=schedulesSessions.get(i);
            if(newSession.overLapsWith(existingSession)){
                return true;
            }
        }
        return false;
    }


}
