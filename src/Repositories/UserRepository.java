package Repositories;

import Model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the storage and retrieval of User objects.
 */
public class UserRepository {
    private Map<String, User> users;

    public UserRepository() {
        this.users = new HashMap<>();
    }

    public void add(User user) {
        users.put(user.getId(), user);
    }

    public User getById(String id) {
        return users.get(id);
    }

    public User getByEmail(String email) {
        for (User u : users.values()) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    /** * Loads User data from Users.csv.
     * Requires StudentGroupRepository to link Student objects to their groups.
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

    /** Saves all User data to Users_out.csv using PrintWriter */
    public void saveData() {
        String filePath = "output/csv/Users_out.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            // Write CSV header
            pw.println("ID,Name,Email,Password,Role,Group_ID");

            for (User user : users.values()) {
                // Determine group ID if Student, else leave empty
                String groupId = "";
                if (user instanceof Student) {
                    groupId = ((Student) user).getStudentGroup().getGroupId();
                }

                String line = String.join(",",
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(), // assuming password is stored in plain text; ideally hashed
                        user.getRole(),
                        groupId
                );

                pw.println(line);
            }
            System.out.println("Users saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving users to " + filePath + ": " + e.getMessage());
        }
    }

}