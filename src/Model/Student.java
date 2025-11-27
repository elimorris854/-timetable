package Model;

import java.util.List;

/**
 * Represents a student enrolled in a programme.
 * A student belongs to a student group and has an associated timetable.
 */
public class Student extends User {

    /** The student group this student belongs to. */
    private String studentGroupID;

    /** The timetable specific to this student. */
    private Timetable timetable;

    /**
     * Constructor for Student.
     *
     * @param id unique identifier
     * @param name full name
     * @param email email address
     * @param studentGroupID the ID of the group the student is part of
     */
    public Student(String id, String name, String email, String studentGroupID) {
        super(id, name, email);
        this.studentGroupID = studentGroupID;
        this.timetable = new Timetable("Student-" + id);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getStudentGroupID() {
        return studentGroupID;
    }

    public Timetable getTimetable() {
        return timetable;
    }
}

