package models;

import java.io.Serializable;
import com.MainApplication;

public abstract class User implements Login, Serializable {
    private static final long serialVersionUID = 1L;

    protected String username;
    protected String password;
    protected String date;

    public User(String username, String password, String date) {
        this.username = username;
        this.password = password;
        this.date = date;
        // Add user to database
        Database.getInstance().addUser(this);
    }

    public User() {
        // Default constructor
    }

    @Override
    public boolean authenticate(String username, String password) {
        return (this.username.equals(username) && this.password.equals(password));
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        if (authenticate(this.username, this.password)) {
            System.out.println(this.username + " is authenticated");
            this.password = password;
        } else {
            System.out.println(this.username + " is not authenticated");
        }
    }
}