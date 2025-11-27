package Repositories;

import Model.Room;
import Model.Student;
import Model.StudentGroup;
import Model.Timetable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

/**
 * Repository for storing StudentGroup objects.
 *
 * <p>A StudentGroup represents a set of students enrolled in a specific
 * year/programme, which may be split into smaller groups for lab/tutorial sessions.</p>
 */
public class StudentGroupRepository {

    /** List storing all student groups. */
    private List<StudentGroup> studentGroups = new ArrayList<>();

    /**
     * Adds a student group to the repository.
     *
     * @param studentGroup the StudentGroup object to add
     */
    public void add(StudentGroup studentGroup) {studentGroups.add(studentGroup);}

    /**
     * Returns all student groups stored in the repository.
     *
     * @return list of all student groups
     */
    public List<StudentGroup> getAll() { return studentGroups; }

    /**
     * Loads student groups from a CSV file.
     * CSVs should be of format groupID,programmeCode,year
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                studentGroups.add(new StudentGroup(parts[0], (parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + e);
        }
    }

    /**
     * Saves all student groups to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.println("GroupID," + "ProgrammeCode," + "Year");
            for(StudentGroup sg : studentGroups) {
                pw.println(sg.getGroupId() + "," +
                        sg.getProgrammeCode() + "," +
                        sg.getYear());
            }
        } catch (Exception e) {
            System.out.println("Error saving csv file " + e);
        }
    }
}

