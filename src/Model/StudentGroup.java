package Model;

import java.util.List;
import java.util.*;

/**
 * Represents a group of students who share a programme.
 * A student group has students and has an associated timetable.
 */

public class StudentGroup{

/** Unique identifier for the group (e.g., "CS1A"). */
/** Code of the programme this group belongs to. */
private String programmeCode;



    /** Year of the programme. */
private int year;

private String groupId;

/** List of student IDs within the group. */
private List<String> studentIds;

private Timetable timetable;

/** Mapping from subgroup ID to list of student IDs. */
private Map<String, List<String>> subgroups;


public StudentGroup(String groupId, String programmeCode, int year) {
this.groupId = groupId;
this.programmeCode = programmeCode;
this.year = year;
this.studentIds = new ArrayList<>();
this.subgroups = new HashMap<>();
this.timetable = new Timetable(groupId);
}

public String getGroupId() {
        return groupId;
}

public Timetable getTimetable() {
        return timetable;
}

    public int getYear() {
        return year;
    }

    public String getProgrammeCode() {
        return programmeCode;
    }
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


/** Converts the group to a CSV row. */
public String toCSV() {
String students = String.join("|", studentIds);


List<String> subgroupParts = new ArrayList<>();
for (String sgId : subgroups.keySet()) {
subgroupParts.add(sgId + ":" + String.join("|", subgroups.get(sgId)));
}


return groupId + "," + programmeCode + "," + year + "," + students + "," + String.join(";", subgroupParts);
}
  
}
