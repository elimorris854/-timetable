package Repositories;

import Model.Module;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
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
    public void add(Module module) {
        modules.add(module);
    }

    /**
     * Returns all modules stored in the repository.
     *
     * @return list of all modules
     */
    public List<Module> getAll() { return modules; }

    /**
     * Loads modules from a CSV file.
     * CSVs should be in format code,lectureHours,tutorialHours,labHours
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                modules.add(new Module(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + e);
        }
    }

    /**
     * Saves all modules to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.println("Code," + "LectureHours," + "TutorialHours," + "LabHours");
            for(Module m : modules) {
                pw.println(m.getCode() + "," +
                           m.getLectureHours() + "," +
                           m.getTutorialHours() + "," +
                           m.getLabHours());
            }
        } catch (Exception e) {
            System.out.println("Error saving csv file " + e);
        }
    }
}
