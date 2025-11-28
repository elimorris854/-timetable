package Model;
/**
 * Abstract base class representing a system user.
 * Provides common attributes and methods shared by all user types.
 */
public abstract class User {

    /** Unique identifier for the user. */
    protected String id;

    /** Full name of the user. */
    protected String name;

    /** Email address of the user. */
    protected String email;

    /** Password of the user. */
    protected String password;

    /**
     * Constructor for creating a user.
     *
     * @param id unique identifier
     * @param name full name
     * @param email email address
     */
    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ----- Getters -----

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {return password;}

    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }

    public abstract String getRole();
}