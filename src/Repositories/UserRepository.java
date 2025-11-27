package Repositories;

import Model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
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
    public void add(User user) {users.add(user);}

    /**
     * Returns all users stored in the repository.
     *
     * @return list of all users
     */
    public List<User> getAll() { return users; }

    /**
     * Loads users from a CSV file.
     * CSVs should be of format ID,role,name,email,studentGroupID(optional)
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String ID = parts[0];
                String role = parts[1];
                String name = parts[2];
                String email = parts[3];
                User user;
                switch(role.toUpperCase()) {
                    case "ADMIN" -> {
                        user = new Admin(ID, name, email);
                    }
                    case "LECTURER" -> {
                        user = new Lecturer(ID, name, email);
                    }
                    case "STUDENT" -> {
                        String groupID = parts.length >= 5 ? parts[4] : "";
                        user = new Student(ID, name, email, groupID);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + e);
        }
    }

    /**
     * Saves all users to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.println("ID," + "Role," + "Name," + "EMail," + "StudentGroupID");
            for(User u : users) {
                if (!(u instanceof Student)) {
                    pw.println(u.getId() + "," +
                            u.getRole() + "," +
                            u.getName() + "," +
                            u.getEmail());
                } else {
                    pw.println(u.getId() + "," +
                            u.getRole() + "," +
                            u.getName() + "," +
                            u.getEmail() + "," +
                            ((Student) u).getStudentGroupID());
                }
            }
        } catch (Exception e) {
            System.out.println("Error saving csv file " + e);
        }
    }
}
