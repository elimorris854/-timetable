package Repositories;

import Model.Module;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the storage and retrieval of Module objects.
 */
public class ModuleRepository {
    private Map<String, Module> modules;

    public ModuleRepository() {
        this.modules = new HashMap<>();
    }

    public void add(Module module) {
        modules.put(module.getCode(), module);
    }

    public Module getByCode(String code) {
        return modules.get(code);
    }

    public Module getById(String code) {
        return modules.get(code);
    }

    public List<Module> getAll() {
        return new ArrayList<>(modules.values());
    }

    /** Loads Module data from Modules.csv. */
    public void loadData() {
        String filePath = "Resources/Modules.csv";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine(); // Skip header
            String line;

            while ((line = br.readLine()) != null) {
                // Code, Name, Lecture_Hours, Lab_Hours, Tutorial_Hours, Lecturer_IDs
                String[] data = line.split(",", -1);
                if (data.length != 6) continue;

                String code = data[0].trim();
                String name = data[1].trim();

                try {
                    int lecHrs = Integer.parseInt(data[2].trim());
                    int labHrs = Integer.parseInt(data[3].trim());
                    int tutHrs = Integer.parseInt(data[4].trim());

                    // Parse pipe-separated Lecturer IDs
                    List<String> lecturerIds = new ArrayList<>();
                    if (!data[5].trim().isEmpty()) {
                        lecturerIds = Arrays.asList(data[5].split("\\|"));
                    }

                    Module module = new Module(code, name, lecHrs, labHrs, tutHrs, lecturerIds);
                    add(module);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping module " + code + ": Invalid hour format.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data from " + filePath + ": " + e.getMessage());
        }
    }

    public void saveData() {
        String filePath = "Resources/Modules_out.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            // Write header
            writer.println("Code,Name,Lecture_Hours,Lab_Hours,Tutorial_Hours,Lecturer_IDs");

            // Write each module as a CSV line
            for (Module module : modules.values()) {
                String lecturerIds = String.join("|", module.getLecturerIds());

                writer.printf("%s,%s,%d,%d,%d,%s%n",
                        module.getCode(),
                        module.getName(),
                        module.getLectureHours(),
                        module.getLabHours(),
                        module.getTutorialHours(),
                        lecturerIds
                );
            }

            System.out.println("Module data saved to: " + filePath);

        } catch (IOException e) {
            System.err.println("Error saving data to " + filePath + ": " + e.getMessage());
        }
    }
}
