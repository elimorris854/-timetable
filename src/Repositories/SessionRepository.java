package Repositories;

import Model.Session;
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
    public void add(Session session) {}

    /**
     * Returns all sessions stored in the repository.
     *
     * @return list of all sessions
     */
    public List<Session> getAll() { return null; }

    /**
     * Loads sessions from a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {}

    /**
     * Saves all sessions to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {}
}

