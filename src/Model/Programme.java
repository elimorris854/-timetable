package Model;

import java.util.List;
import java.util.*;
/**
 * Represents a programme with a number of module assigned to it.
 * Each programme has one student group.
 */

public class Programme {
    private String code;
    private String name;
    private int year;
    private int semester;
    private List<String> moduleCodes;

    /**
     * Programme code (e.g., "CS").
     */
    public Programme(String code, String name, int year, int semester, List<String> moduleCodes) {
        this.code = code;
        this.year = year;
        this.semester = semester;
        this.code = code;
        this.moduleCodes = moduleCodes;
    }

    /**
     * returns programme name
     *
     */
    public String getName() {
        return name;
    }

    /**
     * returns programme code
     *
     */
    public String getCode(){
        return code;
    }

    /**
     * returns programme year
     *
     */
    public int getYear() {
        return year;
    }

    /**
     * returns programme semester
     *
     */
    public int getSemester() {
        return semester;
    }
    /**
     * returns programme module
     *
     */
    public List<String> getModuleCodes(){
        return moduleCodes;
    }

}
