package Repositories;

import Model.*;
import java.io.*;
import java.time.DayOfWeek; // Required for Day enum
import java.time.LocalTime; // Required for Time parsing
import java.util.*;

/**
 * Manages the storage and retrieval of Session objects.
 */
public class SessionRepository {
    private Map<String, Session> sessions;

    public SessionRepository() {
        this.sessions = new HashMap<>();
    }

    public void add(Session session) {
        sessions.put(session.getSessionID(), session);
    }

    public Session getById(String id) {
        return sessions.get(id);
    }

    public List<Session> getAll() {
        return new ArrayList<>(sessions.values());
    }

    public void remove(String id) {
        sessions.remove(id);
    }

    /** * Loads Session data from Sessions.csv and schedules them into all relevant Timetables.
     */
    public void loadData(UserRepository userRepo, RoomRepository roomRepo,
                         StudentGroupRepository groupRepo, ModuleRepository moduleRepo) {
        String filePath = "Resources/Sessions.csv";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine(); // Skip header
            String line;

            while ((line = br.readLine()) != null) {
                // ID, Module_Code, Type, Lecturer_ID, Room_ID, Group_IDs, Day, Start_Time, Duration_Minutes
                String[] data = line.split(",", -1);
                if (data.length != 9) continue;

                String sessionId = data[0].trim();
                String moduleCode = data[1].trim();
                String sessionType = data[2].trim();
                String lecturerId = data[3].trim();
                String roomId = data[4].trim();

                List<String> groupIds = new ArrayList<>();
                if (!data[5].trim().isEmpty()) {
                    // Parse pipe-separated Group IDs
                    groupIds = Arrays.asList(data[5].split("\\|"));
                }

                try {
                    DayOfWeek day = DayOfWeek.valueOf(data[6].trim().toUpperCase());
                    LocalTime startTime = LocalTime.parse(data[7].trim());
                    int duration = Integer.parseInt(data[8].trim());

                    Session session = new Session(sessionId, moduleCode, sessionType, lecturerId,
                            roomId, groupIds, day, startTime, duration);

                    // --- CRITICAL STEP: SCHEDULE THE SESSION ---

                    // 1. Get entities
                    Room room = roomRepo.getById(roomId);
                    User user = userRepo.getById(lecturerId);

                    if (room == null) {
                        System.err.println("Skipping session " + sessionId + ": Room " + roomId + " not found.");
                        continue;
                    }
                    if (user == null || !(user instanceof Lecturer)) {
                        System.err.println("Skipping session " + sessionId + ": Lecturer " + lecturerId + " not found or is not a Lecturer.");
                        continue;
                    }
                    Lecturer lecturer = (Lecturer) user;

                    // 2. Conflict Check and Commit (Sequential, with rollback logic)

                    // Add to Room's timetable
                    if (!room.getTimetable().addSession(session)) {
                        System.err.println("Conflict detected: Room " + roomId + " is busy for session " + sessionId);
                        continue;
                    }

                    // Add to Lecturer's timetable
                    if (!lecturer.getTimetable().addSession(session)) {
                        System.err.println("Conflict detected: Lecturer " + lecturerId + " is busy for session " + sessionId);
                        room.getTimetable().removeSession(sessionId); // Rollback
                        continue;
                    }

                    // Add to all Student Groups' timetables
                    boolean groupConflict = false;
                    List<StudentGroup> committedGroups = new ArrayList<>();
                    for (String groupId : groupIds) {
                        StudentGroup group = groupRepo.getById(groupId);
                        if (group == null) {
                            System.err.println("Warning: Group " + groupId + " not found for session " + sessionId);
                            continue;
                        }
                        if (!group.getTimetable().addSession(session)) {
                            System.err.println("Conflict detected: Group " + groupId + " has a clash for session " + sessionId);
                            groupConflict = true;
                            break;
                        }
                        committedGroups.add(group);
                    }

                    if (groupConflict) {
                        // Rollback all successful additions
                        room.getTimetable().removeSession(sessionId);
                        lecturer.getTimetable().removeSession(sessionId);
                        for (StudentGroup group : committedGroups) {
                            group.getTimetable().removeSession(sessionId);
                        }
                        continue; // Skip adding the session to the repository
                    }

                    // 3. Add to the repository itself (Only if successfully scheduled everywhere)
                    add(session);

                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping session " + sessionId + ": Invalid time/day/duration format: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("An unexpected error occurred while loading session " + sessionId + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data from " + filePath + ": " + e.getMessage());
        }
    }

    /** Saves all Session data to Sessions.csv using PrintWriter */
    public void saveData() {
        String filePath = "output/csv/Sessions_out.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            // Write CSV header
            pw.println("ID,Module_Code,Type,Lecturer_ID,Room_ID,Group_IDs,Day,Start_Time,Duration_Minutes");

            for (Session session : sessions.values()) {
                String groupIds = String.join("|", session.getStudentGroupIDs());

                String line = String.join(",",
                        session.getSessionID(),
                        session.getModuleCode(),
                        session.getSessionType(),
                        session.getLecturerID(),
                        session.getRoomID(),
                        groupIds,
                        session.getDay().toString(),
                        session.getStartTime().toString(),
                        String.valueOf(session.getSessionDuration())
                );

                pw.println(line);
            }
            System.out.println("Sessions saved to " + filePath);

        } catch (IOException e) {
            System.err.println("Error saving sessions to " + filePath + ": " + e.getMessage());
        }
    }

}

