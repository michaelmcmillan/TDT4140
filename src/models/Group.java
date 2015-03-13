package models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Group {
    private int id;
    private int Calendar_id;
    private int supergroup;
    private String name;

    public Group() {

    }

    public void setCalendar_id (int calendar_id) { this.Calendar_id = calendar_id; }
    public int getCalendar_id () { return this.Calendar_id; }

    public void setId (int id) { this.id = id; }
    public int getId () { return this.id; }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public int getSupergroup() {
        return supergroup;
    }

    public void setSupergroup(int supergroup) {
        this.supergroup = supergroup;
    }
}