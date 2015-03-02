package views;

import javafx.scene.shape.Rectangle;

/**
 * Created by Morten on 27.02.15.
 */
public class AppointmentView extends Rectangle {
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
}
