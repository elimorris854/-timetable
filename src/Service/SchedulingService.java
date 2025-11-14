package Service;

import Model.Timetable;

/**
 * Defines the contract for accessing and managing Timetable data.
 * This repository is responsible for loading the schedule (Timetable)
 * for any given resource (Room, Lecturer, or StudentGroup).
 */
public interface SchedulingService{
    /**
     * Retrieves the Timetable associated with a specific resource owner.
     * @param ownerId The unique ID of the resource owner (e.g., Room ID, Lecturer ID, StudentGroup ID).
     * @return The Timetable object containing all scheduled sessions for that owner.
     */
    Timetable getTimetable(String ownerId);
}