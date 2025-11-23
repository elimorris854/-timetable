package Repositories;

import Model.Module;
import Model.Room;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
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
    public void add(Room room) {
        rooms.add(room);
    }

    /**
     * Returns all rooms stored in the repository.
     *
     * @return list of all rooms
     */
    public List<Room> getAll() { return rooms; }

    /**
     * Loads rooms from a CSV file.
     * CSVs should be of format roomId,capacity,ROOMTYPE
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be read
     */
    public void loadFromCSV(String filePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                rooms.add(new Room(parts[0], Integer.parseInt(parts[1]), Room.RoomType.valueOf(parts[2])));
            }
        } catch (Exception e) {
            System.out.println("Error reading csv file " + e);
        }
    }

    /**
     * Saves all rooms to a CSV file.
     *
     * @param filePath path to the CSV file
     * @throws Exception if the file cannot be written
     */
    public void saveToCSV(String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(filePath)) {
            pw.println("RoomID," + "Capacity," + "ROOMTYPE");
            for(Room r : rooms) {
                pw.println(r.getId() + "," +
                        r.getCapacity() + "," +
                        r.getType());
            }
        } catch (Exception e) {
            System.out.println("Error saving csv file " + e);
        }
    }
}

