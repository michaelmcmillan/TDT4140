package models;

import java.util.ArrayList;

public class Person {

    private int id;
    private String firstName;
    private String surname;
    private String email;
    private String password;
    private int alarmTime = 0;
    private int calendarId;

    public Person () {

    }

    public Person(int id, String email, String firstName, String surname) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
    }

    public int getId () {
        return this.id;
    }

    public int getAlarmTime () {
        return this.alarmTime;
    }

    public void setId (int id) {
        this.id = id;
    }

    public void setAlarmTime (int alarmTime) {
        this.alarmTime = alarmTime;
    }


    public void setCalendarId (int calendarId) {
        this.calendarId = calendarId;
    }

    public int getCalendarId () {
        return this.calendarId;
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

}
