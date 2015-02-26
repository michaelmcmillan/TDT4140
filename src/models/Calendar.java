package models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharklaks on 26/02/15.
 */
public class Calendar {

    ArrayList<Appointment> appointments;

    public void addAppointment(Appointment appointment){
        appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment){
        if(appointments.contains(appointment))
            appointments.remove(appointment);
    }

    public boolean overlaps(Appointment appointment1, Appointment appointment2){

        //***MA ENDRES
        return true;
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

}
