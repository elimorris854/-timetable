package Repositories;

import Model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the storage and retrieval of User objects in memory.
 * Provides basic CRUD-like operations and a method to load data from a CSV file.
 */
public class UserRepository {
    private Map<String, User> users;

    /**
     * Initializes a new UserRepository with an empty HashMap to store users.
     */
    public UserRepository() {
        this.users = new HashMap<>();
    }

    /**
     * Adds a User object to the repository.
     * The User's ID is used as the key.
     *
     * @param user The User object to be added.
     */
    public void add(User user) {
        users.put(user.getId(), user);
    }

    /**
     * Retrieves a User object by its unique ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User object corresponding to the ID, or null if not found.
     */
    public User getById(String id) {
        return users.get(id);
    }

    /**
     * Retrieves a User object by their email address. The search is case-insensitive.
     *
     * @param email The email address of the user to retrieve.
     * @return The User object with the matching email, or null if not found.
     */
    public User getByEmail(String email) {
        for (User u : users.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Returns a List containing all User objects currently in the repository.
     * The list is a shallow copy of the internal collection.
     *
     * @return A List of all User objects.
     */
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    /**
     * Loads User data from the "Resources/Users.csv" file.
     * It reads each line, splits the data, and instantiates the appropriate subclass
     * (Admin, Lecturer, or Student) based on the 'Role' field.
     * Students are linked to their respective StudentGroup using the provided repository.
     * Errors during file reading or group linking are printed to System.err.
     *
     * @param groupRepo The StudentGroupRepository required to fetch group details for Students.
     */
    public void loadData(StudentGroupRepository groupRepo) {
        String filePath = "Resources/Users.csv";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine(); // Skip header
            String line;

            while ((line = br.readLine()) != null) {
                // ID, Name, Email, Password, Role, Group_ID
                String[] data = line.split(",", -1);
                if (data.length != 6) continue;

                String id = data[0].trim();
                String name = data[1].trim();
                String email = data[2].trim();
                String password = data[3].trim();
                String role = data[4].trim();
                String groupId = data[5].trim();

                User user = null;

                switch (role) {
                    case "Admin":
                        user = new Admin(id, name, email, password);
                        break;
                    case "Lecturer":
                        user = new Lecturer(id, name, email, password);
                        break;
                    case "Student":
                        StudentGroup group = groupRepo.getById(groupId);
                        if (group != null) {
                            user = new Student(id, name, email, password, group);
                        } else {
                            System.err.println("Warning: Student " + id + " assigned to non-existent group " + groupId);
                        }
                        break;
                    default:
                        System.err.println("Unknown user role: " + role);
                        continue;
                }

                if (user != null) {
                    add(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data from " + filePath + ": " + e.getMessage());
        }
    }
}