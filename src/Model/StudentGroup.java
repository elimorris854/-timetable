package Model;

import java.util.List;
import java.util.*;

/**
 * Represents a group of students who share a programme.
 * A student group has students and has an associated timetable.
 */
public class StudentGroup {

    /** Unique identifier for the group (e.g., "CS1A"). */
    private String groupId;

    /** Code of the programme this group belongs to. */
    private String programmeCode;

    /** Year of the programme. */
    private int year;

    /** List of student IDs within the group. */
    private List<String> studentIds;

    /** Mapping from subgroup ID to list of student IDs. */
    private Map<String, List<String>> subgroups;

    /** The timetable associated with this group */
    private Timetable timetable;

    public StudentGroup(String groupId, String programmeCode, int year, List<String> studentIds) {
        this.groupId = groupId;
        this.programmeCode = programmeCode;
        this.year = year;
        this.studentIds = studentIds;
        this.subgroups = new HashMap<>();
        this.timetable = new Timetable(groupId);
    }

    public String getGroupId() {
        return groupId;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public String getProgrammeCode() {return programmeCode;}

    public int getYear() {return year;}

    /** Returns number of students in the main group. */
    public int size() {
        return studentIds.size();
    }

    /**
     * Splits the group into n subgroups using round-robin assignment.
     */
    public void createSubgroups(int n) {
        subgroups.clear();
        for (int i = 1; i <= n; i++) {
            subgroups.put("SG" + i, new ArrayList<>());
        }
        for (int i = 0; i < studentIds.size(); i++) {
            String subgroupId = "SG" + ((i % n) + 1);
            subgroups.get(subgroupId).add(studentIds.get(i));
        }
    }

    /** Returns the subgroup a student belongs to, or null if none. */
    public String getSubgroupForStudent(String studentId) {
        for (String sgId : subgroups.keySet()) {
            if (subgroups.get(sgId).contains(studentId)) {
                return sgId;
            }
        }
        return null;
    }

    public void addModules(List<Module> modules) {

    }

    /** Converts the group to a CSV row. */
    public String toCSV() {
        String students = String.join("|", studentIds);
        List<String> subgroupParts = new ArrayList<>();
        for (String sgId : subgroups.keySet()) {
            subgroupParts.add(sgId + ":" + String.join("|", subgroups.get(sgId)));
        }
        return groupId + "," + programmeCode + "," + year + "," + students + "," + String.join(";", subgroupParts);
    }

    public List<String> getStudentIDs() { return studentIds;
    }
}