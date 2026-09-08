package models;

import java.io.Serializable;
import com.MainApplication;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;

    private double balance;

    public Wallet(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            Database.getInstance().saveData();
        }
    }

    public void withdraw(double ticketPrice) {
        balance -= ticketPrice;
        Database.getInstance().saveData();
    }

    @Override
    public String toString() {
        return "Wallet{balance=" + balance + '}';
    }
}