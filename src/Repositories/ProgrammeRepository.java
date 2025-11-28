package Repositories;

import Model.Programme;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the storage and retrieval of Programme objects.
 */
public class ProgrammeRepository {
    private Map<String, Programme> programmes;

    public ProgrammeRepository() {
        this.programmes = new HashMap<>();
    }

    public void add(Programme programme) {
        programmes.put(programme.getCode(), programme);
    }

    public Programme getByCode(String code) {
        return programmes.get(code);
    }

    // Convenience method for UserRepo to get a Programme by code
    public Programme getById(String code) {
        return programmes.get(code);
    }

    public List<Programme> getAll() {
        return new ArrayList<>(programmes.values());
    }

    /** Loads Programme data from Programmes.csv. */
    public void loadData() {
        String filePath = "Resources/Programmes.csv";

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length < 5) {
                    System.out.println("Invalid programme entry: " + line);
                    continue;
                }

                String code = parts[0].trim();
                String name = parts[1].trim();
                int year = Integer.parseInt(parts[2].trim());
                int semester = Integer.parseInt(parts[3].trim());

                List<String> moduleCodes = new ArrayList<>();

                if (!parts[4].trim().isEmpty()) {
                    moduleCodes = Arrays.asList(parts[4].split("\\|"));
                }

                Programme p = new Programme(code, name, year, semester, moduleCodes);
                add(p);
            }

        } catch (Exception e) {
            System.out.println("Error loading programmes: " + e.getMessage());
        }
    }

    public void saveData() {
        String filePath = "output/csv/Programmes_out.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

            // Write header
            writer.println("Code,Name,Year,Semester,Module_Codes");

            // Write each module as a CSV line
            for (Programme p : programmes.values()) {
                String moduleCodes = String.join("|", p.getModuleCodes());

                writer.printf("%s,%s,%d,%d,%s%n",
                        p.getCode(),
                        p.getName(),
                        p.getYear(),
                        p.getSemester(),
                        moduleCodes);
            }

            System.out.println("Programme data saved to: " + filePath);

        } catch (IOException e) {
            System.err.println("Error saving data to " + filePath + ": " + e.getMessage());
        }
    }
}
