package models;

import java.util.ArrayList;

public class Person {

    private String firstName;
    private String surname;
    private String email;
    private String password;
    private int alarmTime = 0;
    private ArrayList<Group> groups;

    public Person(String firstName, String surname, String email, String password) {
        this.setFirstName(firstName);
        this.setSurname(surname);
        this.setEmail(email);
        this.setPassword(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {return surname;}

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail(){return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public int getAlarmTime() {return alarmTime;}

    public void setAlarmTime(int alarmTime) {this.alarmTime = alarmTime;}

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        if (groups.contains(group)) {
            groups.remove(group);
        }
    }
}
