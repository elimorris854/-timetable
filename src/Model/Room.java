package Model;

public class Room {
        private String name;
        private String type;   // "Classroom" or "Lab"
        private int capacity;

        /**
         * Creates a new Room object with the specified details.
         *
         * @param name     the name or identifier of the room (e.g., "CSG001")
         * @param type     the type of the room ("Classroom" or "Lab")
         * @param capacity the maximum number of students the room can hold
         */
        public Room(String name, String type, int capacity) {
            this.name = name;
            this.type = type;
            this.capacity = capacity;
        }

        /**
         * Returns the room name.
         *
         * @return the name of the room
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the room type.
         *
         * @return the type of the room ("Classroom" or "Lab")
         */
        public String getType() {
            return type;
        }

        /**
         * Returns the maximum capacity of the room.
         *
         * @return the room capacity
         */
        public int getCapacity() {
            return capacity;
        }
    }
