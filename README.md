# Event Management System

A JavaFX desktop application built using Object-Oriented Programming (OOP) principles to manage academic events, room reservations, and attendee ticketing.

## Features

- **Admin Dashboard**: Add and manage rooms, set room capacities, and monitor users and events across the system.
- **Organizer Dashboard**: Create, edit, and delete events with date conflict validation and track attendee registration lists.
- **Attendee Dashboard**: Browse available events, purchase tickets with real-time room capacity checks, manage wallet balance, and request refunds.
- **Role-Based Authentication**: Secure login and registration supporting Admin, Organizer, and Attendee roles.
- **Data Persistence**: Automatic saving and loading of entities and relationships using file serialization.

## Built With

- **Java** (JDK 17+)
- **JavaFX** (FXML & Scene Builder)
- **Maven** (Dependency management and build tool)

## Project Structure

```text
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/           # Application entry point
│   │   │   ├── Controllers/   # JavaFX FXML controllers (MVC)
│   │   │   ├── models/        # OOP domain models and Database handler
│   │   │   └── utils/         # Helper classes and session management
│   │   └── resources/
│   │       └── fxml/          # FXML views and UI designs
├── data/                      # Serialized data files (.dat)
└── pom.xml
