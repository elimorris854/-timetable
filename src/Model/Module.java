package Model;

import java.util.List;

/**
 * Represents a module with a specific number of lectures/labs.
 * A module can be used in multiple programmes.
 */
public class Module {

    /** Code of the module (e.g. CS4013) */
    private String code;

    /** Name of the module */
    private String name;

    /** Weekly lecture contact hours. */
    private int lectureHours;

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

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getLectureHours() {
        return lectureHours;
    }

    public int getLabHours() {
        return labHours;
    }

    public int getTutorialHours() {
        return tutorialHours;
    }

    public List<String> getLecturers() {
        return lecturers;
    }
}
