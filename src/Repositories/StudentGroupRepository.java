package Repositories;

import Model.StudentGroup;
import Model.Programme; // Required for ProgrammeRepo parameter (for dependency)
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the storage and retrieval of StudentGroup objects.
 */
public class StudentGroupRepository {
    private Map<String, StudentGroup> groups;

    public StudentGroupRepository() {
        this.groups = new HashMap<>();
    }

    public void add(StudentGroup group) {
        groups.put(group.getGroupId(), group);
    }

    public StudentGroup getById(String id) {
        return groups.get(id);
    }

    public List<StudentGroup> getAll() {
        return new ArrayList<>(groups.values());
    }

    /** * Loads StudentGroup data from StudentGroups.csv.
     * ProgrammeRepository is passed for potential future linking/validation,
     * but not strictly required for the StudentGroup constructor.
     */
    public void loadData(ProgrammeRepository programmeRepo) {
        String filePath = "Resources/StudentGroups.csv";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine(); // Skip header
            String line;

            while ((line = br.readLine()) != null) {
                // ID, Programme_Code, Year, Student_IDs
                String[] data = line.split(",", -1);
                if (data.length != 4) continue;

                String id = data[0].trim();
                String programmeCode = data[1].trim();

                try {
                    int year = Integer.parseInt(data[2].trim());

                    // Parse pipe-separated Student IDs
                    List<String> studentIds = new ArrayList<>();
                    if (!data[3].trim().isEmpty()) {
                        studentIds = new ArrayList<>(Arrays.asList(data[3].split("\\|")));
                    }

                    // A check that the programme exists (optional but recommended)
                    Programme p = programmeRepo.getById(programmeCode);
                    if (p == null) {
                        System.err.println("Warning: Group " + id + " references non-existent Programme " + programmeCode);
                    }

                    StudentGroup group = new StudentGroup(id, programmeCode, year, studentIds);
                    add(group);

                } catch (NumberFormatException e) {
                    System.err.println("Skipping group " + id + ": Invalid year format.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data from " + filePath + ": " + e.getMessage());
        }
    }

    /** Saves all StudentGroup data to StudentGroups.csv using PrintWriter */
    public void saveData() {
        String filePath = "output/csv/StudentGroups_out.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            // Write CSV header
            pw.println("ID,Programme_Code,Year,Student_IDs");

            // Write each group
            for (StudentGroup group : groups.values()) {
                // Join student IDs with pipe '|'
                String studentIds = String.join("|", group.getStudentIDs());

                String line = String.join(",",
                        group.getGroupId(),
                        group.getProgrammeCode(),
                        String.valueOf(group.getYear()),
                        studentIds
                );

                pw.println(line);
            }
            System.out.println("Groups saved to " + filePath);

        } catch (IOException e) {
            System.err.println("Error saving student groups to " + filePath + ": " + e.getMessage());
        }
    }

}
