package Repositories;

import Model.Programme;
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
    public void add(Programme programme) {}

    /**
     * Returns all programmes stored in the repository.
     *
     * @return list of all programmes
     */
    public List<Programme> getAll() { return null; }

    /**
     * Loads programmes from a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {}

    /**
     * Saves all programmes to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {}
}
