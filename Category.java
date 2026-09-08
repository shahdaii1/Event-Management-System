package models;

import java.io.Serializable;
import com.MainApplication;

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Database.getInstance().saveData();
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }
}