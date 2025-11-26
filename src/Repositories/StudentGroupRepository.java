package Repositories;

import Model.StudentGroup;
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
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {

    }

    /**
     * Saves all student groups to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {

    }
}

