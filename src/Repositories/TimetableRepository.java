package Repositories;

import Model.Programme;
import Model.Timetable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
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
     * @param entry the Timetable object to add
     */
    public void add(Timetable entry) {entries.add(entry);}

    /**
     * Returns all timetable entries stored in the repository.
     *
     * @return list of all timetable entries
     */
    public List<Timetable> getAll() { return entries; }

    /**
     * Loads timetable entries from a CSV file.
     * CSVs should be of format ownerID
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                entries.add(new Timetable(parts[0]));
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + e);
        }
    }

    /**
     * Saves all timetable entries to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.println("OwnerID");
            for(Timetable e : entries) {
                pw.println(e.getOwnerID());
            }
        } catch (Exception e) {
            System.out.println("Error saving csv file " + e);
        }
    }
}

