package models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Group {
    private int id;
    private int Calendar_id;
    private String name;
    private ArrayList<Person> members;

    public Group(String name) {
        this.name = name;
        this.members = members;
    }

    public void setCalendar_id (int calendar_id) { this.Calendar_id = calendar_id; }
    public int getCalendar_id () { return this.Calendar_id; }

    public void setId (int id) { this.id = id; }
    public int getId () { return this.id; }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public void addMember (Person person) {
        this.members.add(person);
    }

    public ArrayList<Person> getMembers () {
        return this.members;
    }

    public void removeMember (Person person) {
        this.members.remove(person);
    }

}