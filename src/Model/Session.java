package Model;

/**
 * Defines the fixed set of teaching session types available in the UL Timetabling System.
 * Using an enum enforces type safety and prevents the use of invalid session types.
 */
public enum Session {
    /** Represents a standard lecture session. */
    LECTURE,
    /** Represents a laboratory or practical session. */
    LAB,
    /** Represents a tutorial or smaller group discussion session. */
    TUTORIAL
}