# Health Alert Notification System

A discrete-time event simulation system designed to monitor health incidents
and notify users based on geospatial proximity and severity levels.

---

## Project Overview

This project implements a **Public Health Alert Notification System** that simulates
a simplified version of alert platforms used by public health organizations.

The system processes timestamped health incidents and watcher requests from input files.
Events are handled in chronological order using a discrete-time simulation approach.
Watchers are notified when a health incident occurs within a calculated danger radius,
based on Euclidean distance and severity level.

The project focuses on applying fundamental data structures and event-driven logic
without using built-in Java collection classes.

---

## Key Features

- Discrete-time event simulation
- Custom implementations of linked lists, queues, and positional lists
- Geospatial alert mechanism  
  (notification condition: `distance < 2 × severity`)
- Automatic removal of health incidents older than 6 hours

---

## Required Source Files

Before compiling and running the program, the following Java source files
must be present in the same directory.

### Core Files
- `HealthAlertNotification.java` – Main entry point of the program
- `HealthIncident.java` – Represents a health incident
- `Watcher.java` – Represents a watcher entity

### Data Structures & Interfaces
- `LinkedPositionalList.java`
- `LinkedQueue.java`
- `SinglyLinkedList.java`
- `List.java`
- `Position.java`
- `PositionalList.java`
- `Queue.java`

Text input files (`.txt`) are required only at runtime, not during compilation.

---

## Compilation

Navigate to the directory containing the source files and run:


javac *.java


## Running the Program

The program is executed from the command line using the following syntax:


java HealthAlertNotification [--all] <watcherFile> <healthFile>

#Arguments

--all (optional):
Prints a message every time a health incident is inserted into the system.

<watcherFile>:
Path to the input file containing watcher events (add, delete, query).

<healthFile>:
Path to the input file containing health incident records.

##Example Execution

Standard run:

java HealthAlertNotification watcher_data.txt health_data.txt


Run with full logging enabled:

java HealthAlertNotification --all watcher_data.txt health_data.txt


##Limitations and Additions

-Input files must strictly follow the required format.

-Input events must be sorted chronologically.

-Distance calculations use a Euclidean approximation.
