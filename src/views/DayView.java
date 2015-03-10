package views;

import javafx.scene.layout.Pane;
import java.time.LocalDate;

public class DayView extends Pane {

    LocalDate date;

    public DayView(LocalDate date) {
        super();
        this.date = date;
    }

    public DayView() {
        this(LocalDate.now());
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
