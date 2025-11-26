package Repositories;

import Model.Programme;
import Model.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Repository for storing Session objects.
 *
 * <p>A Session represents a scheduled teaching session, such as a
 * lecture, lab, or tutorial for a student group.</p>
 */
public class SessionRepository {

    /** List storing all sessions. */
    private List<Session> sessions = new ArrayList<>();

    /**
     * Adds a session to the repository.
     *
     * @param session the Session object to add
     */
    public void add(Session session) {sessions.add(session);}

    /**
     * Returns all sessions stored in the repository.
     *
     * @return list of all sessions
     */
    public List<Session> getAll() { return sessions; }

    /**
     * Loads sessions from a CSV file.
     * CSVs should be of format sessionID,moduleCode,sessionType,lecturerID,roomID,studentGroupID1|studentGroupID2,day,startTime,duration
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                List<String> studentGroupIDs = Arrays.asList(parts[5].split("\\|"));
                DayOfWeek day = DayOfWeek.valueOf(parts[6].toUpperCase());
                LocalTime time = LocalTime.parse(parts[7]);
                sessions.add(new Session(parts[0], parts[1], parts[2], parts[3], parts[4], studentGroupIDs, day, time, Integer.parseInt(parts[8])));
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + e);
        }
    }

    /**
     * Saves all sessions to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.println("sessionID,moduleCode,sessionType,lecturerID,roomID,studentGroupID1|studentGroupID2,day,startTime,duration");
            for(Session s : sessions) {
                String studentGroups = String.join("|", s.getStudentGroupIDs());
                pw.println(s.getSessionID() + "," +
                           s.getModuleCode() + "," +
                           s.getSessionType() + "," +
                           s.getLecturerID() + "," +
                           s.getRoomID() + "," +
                           studentGroups + "," +
                           s.getDay() + "," +
                           s.getStartTime() + "," +
                           s.getSessionDuration());
            }
        } catch (Exception e) {
            System.out.println("Error saving csv file " + e);
        }
    }
}

