package models;

import java.util.ArrayList;
import java.util.Date;

public class Calendar {

    private String calendarTitle;
    ArrayList<Appointment> appointments = new ArrayList<Appointment>();

    public Calendar(String title) {
        calendarTitle = title;
    }

    public void setTitle(String title) {
        calendarTitle = title;
    }

    public void addAppointment(Appointment appointment){
        appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment){
        if(appointments.contains(appointment))
            appointments.remove(appointment);
    }
}
