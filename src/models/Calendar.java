package models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharklaks on 26/02/15.
 */
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

    public boolean overlaps(Appointment appointment1, Appointment appointment2){
        Date s1 = appointment1.getStartTime();
        Date s2 = appointment2.getStartTime();
        Date e1 = appointment1.getEndTime();
        Date e2 = appointment2.getEndTime();

        if(s2.after(s1) && s2.before(e1)){
            return true;
        }

        if(e2.after(s1) && e2.before(e1)){
            return true;
        }

        return false;
    }

    public ArrayList<Appointment> getAppointmentsBetween(Date startTime, Date endTime){
        ArrayList<Appointment> appointmentsBetween = new ArrayList<Appointment>();
        for(int i = 0; i < appointments.size(); i++){
            Appointment currentAppointment = appointments.get(i);

            if (startTime.before(currentAppointment.getStartTime()) && endTime.after(currentAppointment.getEndTime())) {
                appointmentsBetween.add(currentAppointment);
            }
        }
        return appointmentsBetween;
    }

    public ArrayList<Appointment> getAppointments() {
        return this.appointments;
    }

    public String toString() {
        return this.calendarTitle;
    }
}
