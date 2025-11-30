# Timetable Project

## Overview

This project is a Java-based **Timetable Management System** designed to
load academic data from CSV files, manage entities such as users,
modules, programmes, rooms, and student groups, and generate timetables
through a scheduling service.

The system follows an **MVC-inspired architecture**: - **Model**:
Represents core domain entities. - **Repositories**: Load and manage
data from CSV files. - **Service**: Implements core scheduling logic. -
**View**: Provides a CLI for user interaction. - **Controller**:
Coordinates user actions between the view and the underlying services.

------------------------------------------------------------------------

## Project Structure

    src/
     ├── Controller/
     │    └── AppController.java
     ├── Model/
     │    ├── Module.java
     │    ├── Programme.java
     │    ├── Room.java
     │    ├── Session.java
     │    ├── StudentGroup.java
     │    └── User.java
     ├── Repositories/
     │    ├── ModuleRepository.java
     │    ├── ProgrammeRepository.java
     │    ├── RoomRepository.java
     │    ├── SessionRepository.java
     │    ├── StudentGroupRepository.java
     │    └── UserRepository.java
     ├── Resources/
     │    ├── Modules.csv
     │    ├── Programmes.csv
     │    ├── Rooms.csv
     │    ├── Sessions.csv
     │    ├── StudentGroups.csv
     │    └── Users.csv
     ├── Service/
     │    └── SchedulingService.java
     ├── View/
     │    └── CLIView.java
     └── TimetablingApp


------------------------------------------------------------------------

## How to Run

1.  Open the project in IntelliJ or your preferred Java IDE.
2.  Ensure the project SDK is set to **Java 17+**.
3.  Run the `CLIView.java` file as configured in your IDE.

------------------------------------------------------------------------

## CSV Files

The following CSV files provide all necessary seed data: -
**Modules.csv** -- module information - **Programmes.csv** -- programme
listings - **Rooms.csv** -- room data including capacities -
**Sessions.csv** -- teaching session definitions - **StudentGroups.csv**
-- student group metadata - **Users.csv** -- user and role data

Each repository automatically loads its corresponding CSV into memory
when instantiated.

------------------------------------------------------------------------

## Requirements

-   Java 17+
-   No external libraries required (uses standard Java APIs)


This project does not include a specified license. Add one if needed.

