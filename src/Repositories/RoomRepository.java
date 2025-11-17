package Repositories;

import Model.Room;
import java.util.List;
import java.util.ArrayList;

/**
 * Repository for storing Room objects.
 */
public class RoomRepository {

    /** List storing all rooms. */
    private List<Room> rooms = new ArrayList<>();

    /**
     * Adds a room to the repository.
     *
     * @param room the Room object to add
     */
    public void add(Room room) {}

    /**
     * Returns all rooms stored in the repository.
     *
     * @return list of all rooms
     */
    public List<Room> getAll() { return null; }

    /**
     * Loads rooms from a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {}

    /**
     * Saves all rooms to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {}
}

