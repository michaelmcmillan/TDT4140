package views;

import javafx.scene.shape.Rectangle;
import models.Appointment;

/**
 * Created by Morten on 27.02.15.
 */
public class AppointmentView extends Rectangle {
    private Appointment appointment;
    private boolean isClicked = false;
    public AppointmentView() {
        super();

    }

    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public Appointment getAppointment(){
        return this.appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
