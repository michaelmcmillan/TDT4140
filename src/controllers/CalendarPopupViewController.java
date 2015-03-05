package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Morten on 02.03.15.
 */
public class CalendarPopupViewController implements Initializable {

    @FXML private TextField titleTextField;
    private ArrayList<Pane> openedCalendarPopups = new ArrayList<Pane>();
    private Pane calendarPane;

    public CalendarPopupViewController(Pane calendarPane){
        this.calendarPane = calendarPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML void addCalendarButtonPressed(ActionEvent event) {
    }

    public void show(){

        try {
            // Init popupview from FXML
            FXMLLoader testLoader = new FXMLLoader(getClass().getResource("../views/CalendarPopupView.fxml"));
            Pane calendarPopup = testLoader.load();
            calendarPopup.setId("calendarPopup");

            // Get controller, add view to main view
            //AppointmentPopupViewController appointmentPopupViewController = testLoader.getController();
            calendarPane.getChildren().add(calendarPopup);
            openedCalendarPopups.add(calendarPopup);

            // Set popup to center position FIX!
            double appointmentPopupWidth = calendarPopup.getWidth();
            double appointmentPopupHeight = calendarPopup.getHeight();
            double mainPaneWidth = calendarPane.getLayoutX();
            double mainPaneHeight = calendarPane.getLayoutY();
            calendarPopup.setLayoutX(mainPaneWidth/2 - appointmentPopupWidth/2);
            calendarPopup.setLayoutY(mainPaneHeight/2 - appointmentPopupHeight/2);

            calendarPopup.setLayoutX(80);
            calendarPopup.setLayoutY(100);

            //Set methods
            Button closeButton = (Button) calendarPopup.lookup("#closeButton");

            closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    close();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        for (int i = 0; i < calendarPane.getChildren().size(); i++) {
            if (openedCalendarPopups.contains(calendarPane.getChildren().get(i))) {
                calendarPane.getChildren().remove(calendarPane.getChildren().get(i));
            }
        }
    }
}
