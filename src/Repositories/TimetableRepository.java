package Repositories;

import Model.Timetable;
import java.util.List;
import java.util.ArrayList;

/**
 * Repository for storing TimetableEntry objects.
 */
public class TimetableRepository {

    /** List storing all timetable entries. */
    private List<Timetable> entries = new ArrayList<>();

    /**
     * Adds a timetable entry to the repository.
     *
     * @param entry the TimetableEntry object to add
     */
    public void add(Timetable entry) {}

    /**
     * Returns all timetable entries stored in the repository.
     *
     * @return list of all timetable entries
     */
    public List<Timetable> getAll() { return null; }

    /**
     * Loads timetable entries from a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {}

    /**
     * Saves all timetable entries to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {}
}

