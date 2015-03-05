package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.Appointment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Morten on 02.03.15.
 */
public class GroupPopupViewController implements Initializable {

    @FXML private TextField titleTextField;
    private ArrayList<Pane> openGroupPopups = new ArrayList<Pane>();
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    private Appointment model;
    private Pane calendarPane;

    public GroupPopupViewController(Pane calendarPane){
        this.calendarPane = calendarPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Appointment();
    }

    @FXML void addGroupButtonPressed(ActionEvent event) {

    }

    public void show(){

        try {
            // Init popupview from FXML
            FXMLLoader testLoader = new FXMLLoader(getClass().getResource("../views/GroupPopupView.fxml"));
            Pane groupPopup = testLoader.load();
            groupPopup.setId("groupPopup");

            // Get controller, add view to main view
            //AppointmentPopupViewController appointmentPopupViewController = testLoader.getController();
            calendarPane.getChildren().add(groupPopup);
            openGroupPopups.add(groupPopup);

            // Set popup to center position FIX!
            double appointmentPopupWidth = groupPopup.getWidth();
            double appointmentPopupHeight = groupPopup.getHeight();
            double mainPaneWidth = calendarPane.getLayoutX();
            double mainPaneHeight = calendarPane.getLayoutY();
            groupPopup.setLayoutX(mainPaneWidth/2 - appointmentPopupWidth/2);
            groupPopup.setLayoutY(mainPaneHeight/2 - appointmentPopupHeight/2);

            groupPopup.setLayoutX(80);
            groupPopup.setLayoutY(100);

            //Set methods
            Button closeButton = (Button) groupPopup.lookup("#closeButton");
            TextField startTime = (TextField) groupPopup.lookup("#startTime");
            TextField endTime = (TextField) groupPopup.lookup("#endTime");
            DatePicker appointmentDate = (DatePicker) groupPopup.lookup("#startDatePicker");

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
            if (openGroupPopups.contains(calendarPane.getChildren().get(i))) {
                calendarPane.getChildren().remove(calendarPane.getChildren().get(i));
            }
        }
    }
}
