package Repositories;

import Model.Module;
import Model.Programme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

/**
 * Repository for storing Programme objects.
 */
public class ProgrammeRepository {

    /** List storing all programmes. */
    private List<Programme> programmes = new ArrayList<>();

    /**
     * Adds a programme to the repository.
     *
     * @param programme the Programme object to add
     */
    public void add(Programme programme) {
        programmes.add(programme);
    }

    /**
     * Returns all programmes stored in the repository.
     *
     * @return list of all programmes
     */
    public List<Programme> getAll() { return programmes; }

    /**
     * Loads programmes from a CSV file.
     * CSVs should be of format year,semester,code
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                programmes.add(new Programme(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), parts[2]));
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + e);
        }
    }

    /**
     * Saves all programmes to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.println("Year," + "Semester," + "Code");
            for(Programme p : programmes) {
                pw.println(p.getYear() + "," +
                        p.getSemester() + "," +
                        p.getCode());
            }
        } catch (Exception e) {
            System.out.println("Error saving csv file " + e);
        }
    }
}
