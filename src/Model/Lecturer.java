package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a lecturer who teaches modules.
 * A lecturer can have multiple assigned modules and a timetable.
 */
public class Lecturer extends User {

    /** List of modules taught by this lecturer. */
    private List<Module> assignedModules;

    /** Lecturer's personal timetable. */
    private Timetable timetable;

    /**
     * Constructor for Lecturer.
     *
     * @param id unique identifier
     * @param name full name
     * @param email email address
     */
    public Lecturer(String id, String name, String email) {
        super(id, name, email);
        this.assignedModules = new ArrayList<>();
        this.timetable = new Timetable("Lecturer-" + id);
    }

    @Override
    public String getRole() {
        return "Lecturer";
    }

    public List<Module> getAssignedModules() {
        return assignedModules;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    /**
     * Assign a module for this lecturer to teach.
     *
     * @param module module to be assigned
     */
    public void assignModule(Module module) {
        if (!assignedModules.contains(module)) {
            assignedModules.add(module);
        }
    }
}

