package Model;

/**
 * Represents a physical room within the university
 */
public class Room {
    private final String id;
    private final int capacity;
    private final RoomType type;
    private final Timetable timetable;


    /**
     * Defines the set of valid types for any room.
     */
    public static enum RoomType {
        CLASSROOM,
        LAB
    }

    /**
     * Constructs a new Room object, initializing its physical attributes and creating its Timetable.
     *
     * @param id The unique id for the room (e.g., "KB1-001").
     * @param capacity The maximum number of people the room can hold.
     * @param type The type of room, defining its usage (e.g., CLASSROOM or LAB).
     */
    public Room(String id, int capacity, RoomType type) {
        this.id = id;
        this.capacity = capacity;
        this.type = type;
        this.timetable = new Timetable(id); // Timetable is owned by the Room's ID
    }

    /**
     * Retrieves the unique id for the room.
     *
     * @return The room ID string.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the maximum capacity of the room (the number of students it can hold).
     * @return The room's capacity (integer).
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Retrieves the specific type of the room (e.g., CLASSROOM or LAB).
     * @return The {@link RoomType} enum value.
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Provides the timetable object associated with this room.
     * @return The {@link Timetable} instance for this room.
     */
    public Timetable getTimetable() {
        return timetable;
    }
}