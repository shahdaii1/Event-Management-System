package models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.MainApplication;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private Date date;
    private double ticketPrice;
    private Room room;
    private Organizer organizer;
    private Category category;
    private List<Attendee> attendees;

    public void setAttendees(List<Attendee> attendees) {
        this.attendees = attendees;
        Database.getInstance().saveData();
    }

    public void setCategory(Category category) {
        this.category = category;
        Database.getInstance().saveData();
    }

    public void setDate(Date date) {
        this.date = date;
        Database.getInstance().saveData();
    }

    public void setDescription(String description) {
        this.description = description;
        Database.getInstance().saveData();
    }

    public void setName(String name) {
        this.name = name;
        Database.getInstance().saveData();
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
        Database.getInstance().saveData();
    }

    public void setRoom(Room room) {
        this.room = room;
        Database.getInstance().saveData();
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
        Database.getInstance().saveData();
    }

    public Event(Category category, Date date, String description, String name, Organizer organizer, Room room, double ticketPrice) {
        this.category = category;
        this.date = date;
        this.description = description;
        this.name = name;
        this.organizer = organizer;
        this.room = room;
        this.ticketPrice = ticketPrice;
        this.attendees = new ArrayList<>();

        // Add event to database
        Database.getInstance().addEvent(this);
    }

    public List<Attendee> getAttendees() {
        return attendees;
    }

    public Category getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public Room getRoom() {
        return room;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public boolean registerAttendee(Attendee attendee) {
        // Check if the room capacity is reached
        if (attendees.size() >= room.getCapacity()) {
            return false; // Room at capacity
        }

        // Room has space, add the attendee
        attendees.add(attendee);
        Database.getInstance().saveData();
        return true;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", ticketPrice=" + ticketPrice +
                ", room=" + room +
                ", category=" + category +
                '}';
    }
}