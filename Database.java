package models;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {
    // File paths for serialized data
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.dat";
    private static final String EVENTS_FILE = DATA_DIR + "/events.dat";
    private static final String ROOMS_FILE = DATA_DIR + "/rooms.dat";
    private static final String CATEGORIES_FILE = DATA_DIR + "/categories.dat";

    // Collection to store all system entities
    private static List<User> users = new ArrayList<>();
    private static List<Event> events = new ArrayList<>();
    private static List<Room> rooms = new ArrayList<>();
    private static List<Category> categories = new ArrayList<>();

    // Singleton instance
    private static Database instance;

    // Private constructor for singleton pattern
    private Database() {
        // Create data directory if it doesn't exist
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Load data from files
        loadData();
    }

    // Get singleton instance
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Save all data to files
    public void saveData() {
        saveToFile(users, USERS_FILE);
        saveToFile(events, EVENTS_FILE);
        saveToFile(rooms, ROOMS_FILE);
        saveToFile(categories, CATEGORIES_FILE);
        System.out.println("All data saved to files");
    }

    // Load all data from files
    @SuppressWarnings("unchecked")
    private void loadData() {
        // Load users
        Object loadedUsers = loadFromFile(USERS_FILE);
        if (loadedUsers != null) {
            users = (List<User>) loadedUsers;
            System.out.println("Loaded " + users.size() + " users");
        }

        // Load categories (load these before events that need them)
        Object loadedCategories = loadFromFile(CATEGORIES_FILE);
        if (loadedCategories != null) {
            categories = (List<Category>) loadedCategories;
            System.out.println("Loaded " + categories.size() + " categories");
        }

        // Load rooms (load these before events that need them)
        Object loadedRooms = loadFromFile(ROOMS_FILE);
        if (loadedRooms != null) {
            rooms = (List<Room>) loadedRooms;
            System.out.println("Loaded " + rooms.size() + " rooms");
        }

        // Load events
        Object loadedEvents = loadFromFile(EVENTS_FILE);
        if (loadedEvents != null) {
            events = (List<Event>) loadedEvents;
            System.out.println("Loaded " + events.size() + " events");
        }

        // Reconstruct the relationships between events and attendees
        reconstructRelationships();
    }

    // Reconstruct relationships between objects after loading from files
    private void reconstructRelationships() {
        // For each attendee, update their joined events
        for (User user : users) {
            if (user instanceof Attendee) {
                Attendee attendee = (Attendee) user;
                List<Event> joinedEvents = new ArrayList<>();

                // Check each event if this attendee is in its attendee list
                for (Event event : events) {
                    boolean isAttending = false;
                    for (Attendee eventAttendee : event.getAttendees()) {
                        if (eventAttendee.getUsername().equals(attendee.getUsername())) {
                            isAttending = true;
                            break;
                        }
                    }

                    if (isAttending) {
                        joinedEvents.add(event);
                    }
                }

                // Update the attendee's events joined list
                attendee.getEventsJoined().clear();
                attendee.getEventsJoined().addAll(joinedEvents);
            }
        }
        System.out.println("Relationships reconstructed");
    }

    // Generic method to save object to file
    private void saveToFile(Object obj, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(obj);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + filePath);
            e.printStackTrace();
        }
    }

    // Generic method to load object from file
    private Object loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file: " + filePath);
            e.printStackTrace();
            return null;
        }
    }

    // User management methods
    public void addUser(User user) {
        users.add(user);
        saveData(); // Save after adding user
    }

    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean updateUser(String username, String newPassword) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setPassword(newPassword);
                saveData(); // Save after updating user
                return true;
            }
        }
        return false;
    }

    public boolean deleteUser(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                // Remove user from events they're attending
                for (Event event : events) {
                    List<Attendee> attendees = event.getAttendees();
                    for (int j = 0; j < attendees.size(); j++) {
                        if (attendees.get(j).getUsername().equals(username)) {
                            attendees.remove(j);
                            break;
                        }
                    }
                }
                users.remove(i);
                saveData(); // Save after deleting user
                return true;
            }
        }
        return false;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    // Added for Main.java compatibility
    public User findUserByUsername(String username) {
        return getUserByUsername(username);
    }

    // Event management methods
    public void addEvent(Event event) {
        events.add(event);
        saveData(); // Save after adding event
    }

    public Event getEventByName(String name) {
        for (Event event : events) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }

    public boolean updateEvent(String oldName, String newName, String newDescription) {
        for (Event event : events) {
            if (event.getName().equals(oldName)) {
                event.setName(newName);
                event.setDescription(newDescription);
                saveData(); // Save after updating event
                return true;
            }
        }
        return false;
    }

    public boolean deleteEvent(String eventName) {
        Event event = getEventByName(eventName);
        if (event != null) {
            for (Attendee attendee : event.getAttendees()) {
                attendee.getWallet().deposit(event.getTicketPrice());
                attendee.getEventsJoined().removeIf(e -> e.getName().equals(eventName));
            }
            events.remove(event);
            saveData(); // Save after deleting event
            return true;
        }
        return false;
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    // New method to remove an attendee from an event
    public boolean removeAttendeeFromEvent(Event event, Attendee attendee) {
        if (event != null && attendee != null) {
            boolean removed = event.getAttendees().removeIf(a -> a.getUsername().equals(attendee.getUsername()));
            if (removed) {
                saveData(); // Save after removing attendee from event
            }
            return removed;
        }
        return false;
    }

    // Room management methods
    public void addRoom(Room room) {
        rooms.add(room);
        saveData(); // Save after adding room
    }

    public Room getRoomByNumber(int roomNo) {
        for (Room room : rooms) {
            if (room.getRoomNo() == roomNo) {
                return room;
            }
        }
        return null;
    }

    public boolean updateRoom(int roomNo, int newCapacity) {
        for (Room room : rooms) {
            if (room.getRoomNo() == roomNo) {
                // Check if any events in this room have more attendees than the new capacity
                for (Event event : events) {
                    if (event.getRoom().getRoomNo() == roomNo &&
                            event.getAttendees().size() > newCapacity) {
                        return false; // Cannot reduce capacity below current attendee count
                    }
                }

                room.setCapacity(newCapacity);
                saveData(); // Save after updating room
                return true;
            }
        }
        return false;
    }

    public boolean deleteRoom(int roomNo) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNo() == roomNo) {
                boolean canDelete = true;
                for (Event event : events) {
                    if (event.getRoom().getRoomNo() == roomNo) {
                        canDelete = false;
                        break;
                    }
                }
                if (canDelete) {
                    rooms.remove(i);
                    saveData(); // Save after deleting room
                    return true;
                }
            }
        }
        return false;
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    // Category management methods
    public void addCategory(Category category) {
        categories.add(category);
        saveData(); // Save after adding category
    }

    public Category getCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public boolean updateCategory(String oldName, String newName) {
        for (Category category : categories) {
            if (category.getName().equals(oldName)) {
                category.setName(newName);
                saveData(); // Save after updating category
                return true;
            }
        }
        return false;
    }

    public boolean deleteCategory(String name) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(name)) {
                boolean canDelete = true;
                for (Event event : events) {
                    if (event.getCategory().getName().equals(name)) {
                        canDelete = false;
                        break;
                    }
                }
                if (canDelete) {
                    categories.remove(i);
                    saveData(); // Save after deleting category
                    return true;
                }
            }
        }
        return false;
    }

    public List<Event> getEventsByCategory(Category category) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getCategory().getName().equals(category.getName())) {
                result.add(event);
            }
        }
        return result;
    }

    public List<Event> getEventsByOrganizer(Organizer organizer) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getOrganizer() != null &&
                    event.getOrganizer().getUsername().equals(organizer.getUsername())) {
                result.add(event);
            }
        }
        return result;
    }

    public List<Attendee> getAttendeesByEvent(Event event) {
        return event.getAttendees();
    }

    // Find available rooms for a given date
    public List<Room> getAvailableRooms(Date date) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.checkRoom(room.getRoomNo(), date)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    // Reset database (for testing)
    public void reset() {
        users.clear();
        events.clear();
        rooms.clear();
        categories.clear();

        // Delete data files
        new File(USERS_FILE).delete();
        new File(EVENTS_FILE).delete();
        new File(ROOMS_FILE).delete();
        new File(CATEGORIES_FILE).delete();

        System.out.println("Database reset and files deleted");
    }
}