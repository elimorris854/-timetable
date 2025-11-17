package Repositories;

import Model.Module;
import java.util.List;
import java.util.ArrayList;

/**
 * Repository for storing Module objects.
 */
public class ModuleRepository {

    /** List storing all modules. */
    private List<Module> modules = new ArrayList<>();

    /**
     * Adds a module to the repository.
     *
     * @param module the Module object to add
     */
    public void add(Module module) {}

    /**
     * Returns all modules stored in the repository.
     *
     * @return list of all modules
     */
    public List<Module> getAll() { return null; }

    /**
     * Loads modules from a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {}

    /**
     * Saves all modules to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {}
}
