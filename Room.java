package models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.MainApplication;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private int roomNo;
    private int capacity;
    private String availableHours;
    private static int roomCount;
    private Date date;

    public void setAvailableHours(String availableHours) {
        this.availableHours = availableHours;
        Database.getInstance().saveData();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
        Database.getInstance().saveData();
    }

    public void setDate(Date date) {
        this.date = date;
        Database.getInstance().saveData();
    }

    public static void setRoomCount(int roomCount) {
        Room.roomCount = roomCount;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
        Database.getInstance().saveData();
    }

    public Room(int roomNo, int capacity, Date date) {
        this.roomNo = roomNo;
        this.date = date;
        this.capacity = capacity;
        roomCount++;
    }

    public Room(int roomNo) {
        this.roomNo = roomNo;
        roomCount++;
    }

    public Room() {
        roomCount++;
    }

    public String getAvailableHours() {
        return availableHours;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public int getCapacity() {
        return capacity;
    }

    public static int getRoomCount() {
        return roomCount;
    }

    public boolean checkRoom(int roomNo, Date date) {
        // Check if this room is already booked for this date
        Database db = Database.getInstance();
        List<Event> allEvents = db.getAllEvents();

        for (Event event : allEvents) {
            // Skip events that don't use this room
            if (event.getRoom().getRoomNo() != roomNo) {
                continue;
            }

            // Compare dates - using same day check (ignoring time)
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(event.getDate());
            cal2.setTime(date);

            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if (sameDay) {
                return false; // Room is already booked for this date
            }
        }

        return true; // Room is available
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNo=" + roomNo +
                ", capacity=" + capacity +
                ", availableHours='" + availableHours + '\'' +
                '}';
    }
}