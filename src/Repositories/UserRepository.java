package Repositories;

import Model.User;
import java.util.List;
import java.util.ArrayList;

/**
 * Repository for storing all User objects (Students, Lecturers, Admins).
 */
public class UserRepository {

    /** List storing all users. */
    private List<User> users = new ArrayList<>();

    /**
     * Adds a user to the repository.
     *
     * @param user the User object to add
     */
    public void add(User user) {}

    /**
     * Returns all users stored in the repository.
     *
     * @return list of all users
     */
    public List<User> getAll() { return null; }

    /**
     * Loads users from a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {}

    /**
     * Saves all users to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {}
}
