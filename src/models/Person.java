package models;

import java.util.ArrayList;

public class Person {

    private String firstName;
    private String surname;
    private ArrayList<Group> groups;

    public Person(String firstName, String surname) {
        this.setFirstName(firstName);
        this.setSurname(surname);
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
}