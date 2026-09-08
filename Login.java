package models;

import java.io.Serializable;

public interface Login extends Serializable {
    public boolean authenticate(String username, String password);
}