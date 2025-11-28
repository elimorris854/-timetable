package Repositories;

import Model.Room;
import Model.Room.RoomType; // Import the inner enum
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the storage and retrieval of Room objects.
 */
public class RoomRepository {
    private Map<String, Room> rooms;

    public RoomRepository() {
        this.rooms = new HashMap<>();
    }

    public void add(Room room) {
        rooms.put(room.getId(), room);
    }

    public Room getById(String id) {
        return rooms.get(id);
    }

    public List<Room> getAll() {
        return new ArrayList<>(rooms.values());
    }

    /** Loads Room data from Rooms.csv. */
    public void loadData() {
        String filePath = "Resources/Rooms.csv";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            br.readLine(); // Skip header
            String line;

            while ((line = br.readLine()) != null) {
                // ID, Capacity, Type (e.g., "KB1-001,100,CLASSROOM")
                String[] data = line.split(",");
                if (data.length != 3) continue;

                String id = data[0].trim();
                String typeStr = data[2].trim().toUpperCase();

                try {
                    int capacity = Integer.parseInt(data[1].trim());
                    // Ensure the RoomType enum is correctly parsed (must be CLASSROOM or LAB)
                    RoomType type = RoomType.valueOf(typeStr);

                    Room room = new Room(id, capacity, type);
                    add(room);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping room " + id + ": Invalid capacity format.");
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping room " + id + ": Invalid RoomType '" + typeStr + "'.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading data from " + filePath + ": " + e.getMessage());
        }
    }

    /** Saves all Room data to Rooms.csv using PrintWriter */
    public void saveData() {
        String filePath = "output/csv/Rooms_out.csv";

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            // Write CSV header
            pw.println("ID,Capacity,Type");

            // Write each room
            for (Room room : rooms.values()) {
                pw.println(room.getId() + "," + room.getCapacity() + "," + room.getType().name());
            }

            System.out.println("Rooms saved successfully to " + filePath);

        } catch (IOException e) {
            System.err.println("Error saving rooms to " + filePath + ": " + e.getMessage());
        }
    }

}

