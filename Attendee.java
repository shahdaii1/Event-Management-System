package models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import com.MainApplication;

public class Attendee extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Wallet wallet;
    private gender Gender;
    private List<Event> eventsJoined;

    public Attendee(String username, String password, String date, gender gender, Wallet wallet) {
        super(username, password, date);
        this.Gender = gender;
        this.wallet = wallet;
        this.eventsJoined = new ArrayList<>();
    }

    public List<Event> getEventsJoined() {
        return eventsJoined;
    }

    public boolean buyTicket(Event event) {
        // First check if wallet has enough funds
        if (wallet.getBalance() < event.getTicketPrice()) {
            return false; // Not enough money
        }

        // Then check if registration is successful (checks room capacity)
        if (!event.registerAttendee(this)) {
            return false; // Room is full
        }

        // If we got here, both checks passed - withdraw money and add to events joined
        wallet.withdraw(event.getTicketPrice());
        eventsJoined.add(event);

        // Save the database after buying the ticket
        Database.getInstance().saveData();

        return true;
    }

    public boolean refundTicket(Event event) {
        if (eventsJoined.contains(event)) {
            wallet.deposit(event.getTicketPrice());
            eventsJoined.remove(event);

            // Save the database after refunding the ticket
            Database.getInstance().saveData();

            return true;
        }
        return false;
    }

    public gender getGender() {
        return Gender;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return "Attendee{" +
                "date='" + date + '\'' +
                ", eventsJoined=" + eventsJoined +
                ", Gender=" + Gender +
                ", wallet=" + wallet +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}