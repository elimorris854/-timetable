package Model;

import java.util.List;

/**
 * Represents a module with a specific number of lectures/labs.
 * A module can be used in multiple programmes.
 */

public class Module {

/** Weekly lab contact hours. */
private int labHours;


/** Weekly tutorial contact hours. */
private int tutorialHours;


/** Lecturers assigned to teach this module. */
private List<String> lecturers;


/**
* Constructor for Module.
*/
public Module(String code, String name, int lectureHours, int labHours, int tutorialHours, List<String> lecturers) {
this.code = code;
this.name = name;
this.lectureHours = lectureHours;
this.labHours = labHours;
this.tutorialHours = tutorialHours;
this.lecturers = lecturers;
}


// ------------------ Getters and Setters ------------------


public String getCode() { return code; }
public void setCode(String code) { this.code = code; }


public String getName() { return name; }
public void setName(String name) { this.name = name; }


public int getLectureHours() { return lectureHours; }
public void setLectureHours(int lectureHours) { this.lectureHours = lectureHours; }


public int getLabHours() { return labHours; }
public void setLabHours(int labHours) { this.labHours = labHours; }


public int getTutorialHours() { return tutorialHours; }
public void setTutorialHours(int tutorialHours) { this.tutorialHours = tutorialHours; }


public List<String> getLecturers() { return lecturers; }
public void setLecturers(List<String> lecturers) { this.lecturers = lecturers; }


/**
* Calculates total contact hours.
*/
public int getTotalContactHours() {
return lectureHours + labHours + tutorialHours;
}


/**
* Converts the module to a CSV row.
*/
public String toCSV() {
return code + "," + name + "," + lectureHours + "," + labHours + "," + tutorialHours + "," + String.join("|", lecturers);
}
  
}
