package models;

import java.util.ArrayList;

public class Person {

    private String firstName;
    private String surname;
    private String username;
    private ArrayList<Group> groups;
    private ArrayList<Calendar> calendars;

    public Person(String username) {
        this.calendars = new ArrayList<Calendar>();
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        if (groups.contains(group)) {
            groups.remove(group);
        }
    }

    public void addCalendar(Calendar calendar) {
        calendars.add(calendar);
    }

    public void removeCalendar(Calendar calendar) {
        if (calendars.contains(calendar)) {
            calendars.remove(calendar);
        }
    }

    public ArrayList<Calendar> getCalendars() {
        return this.calendars;
    }

    public ArrayList<String> getCalendarNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Calendar cal : calendars) {
            names.add(cal.toString());
        }
        return names;
    }
}