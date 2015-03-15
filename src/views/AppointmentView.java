package views;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import models.Appointment;

/**
 * Created by Morten on 27.02.15.
 */
public class AppointmentView extends Rectangle {
    private Appointment appointment;
    private boolean isClicked = false;

    private Text detailsText;
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


    public Text getDetailsText() {
        return detailsText;
    }

    public void setDetailsText(Text detailsText) {
        this.detailsText = detailsText;
    }
}
