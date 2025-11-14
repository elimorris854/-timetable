package Model;

import java.util.List;

/**
 * Represents a student enrolled in a programme.
 * A student belongs to a student group and has an associated timetable.
 */
public class Student extends User {

    /** The student group this student belongs to. */
    private StudentGroup studentGroup;

    /** The timetable specific to this student. */
    private Timetable timetable;

    /**
     * Constructor for Student.
     *
     * @param id unique identifier
     * @param name full name
     * @param email email address
     * @param studentGroup the group the student is part of
     */
    public Student(String id, String name, String email, StudentGroup studentGroup) {
        super(id, name, email);
        this.studentGroup = studentGroup;
        this.timetable = new Timetable("Student-" + id);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public StudentGroup getStudentGroup() {
        return studentGroup;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    /**
     * Enrolls the student in a list of modules via their programme or group.
     *
     * @param modules list of modules to enroll in
     */
    public void enrollInModules(List<Module> modules) {
        studentGroup.addModules(modules);
    }
}

