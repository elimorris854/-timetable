package Model;

import java.util.List;
import java.util.*;
/**
 * Represents a programme with a number of module assigned to it.
 * Each programme has one student group.
 */

public class Programme{

/** Programme code (e.g., "CS"). */
public Programme(String code, String name) {
this.code = code;
this.name = name;
this.structure = new HashMap<>();
}


// ------------------ Structure Management ------------------


/** Adds a module to a specific year + semester. */
public void addModule(int year, int semester, String moduleCode) {
structure.computeIfAbsent(year, y -> new HashMap<>())
.computeIfAbsent(semester, s -> new ArrayList<>())
.add(moduleCode);
}


/** Removes a module from a specific year and semester. */
public boolean removeModule(int year, int semester, String moduleCode) {
if (structure.containsKey(year) && structure.get(year).containsKey(semester)) {
return structure.get(year).get(semester).remove(moduleCode);
}
return false;
}


/** Returns list of module codes for a given year/semester. */
public List<String> getModulesFor(int year, int semester) {
return structure.getOrDefault(year, new HashMap<>())
.getOrDefault(semester, new ArrayList<>());
}


/** Converts programme structure to multiple CSV rows. */
public List<String> toCSVRows() {
List<String> rows = new ArrayList<>();
for (Integer year : structure.keySet()) {
for (Integer sem : structure.get(year).keySet()) {
String modules = String.join("|", structure.get(year).get(sem));
rows.add(code + "," + name + "," + year + "," + sem + "," + modules);
}
}
return rows;
}
  
}
