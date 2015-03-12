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
import models.Person;
import server.Server;
import views.DayView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Morten on 02.03.15.
 */
public class AppointmentPopupViewController  implements Initializable {


    private ArrayList<Pane> openAppointmentPopups = new ArrayList<Pane>();
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    private Appointment model;
    private Pane calendarPane;
    private Pane dayPane;
    TextField   startTime;
    TextField   endTime;
    TextField titleField;
    TextArea formaalField;
    TextField roomField;
    DatePicker  appointmentDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //model = new Appointment();
        //removeEndDayForm(); // Don't show calendar repetition events at startup
    }
/*
    @FXML void addAppointmentButtonPressed(ActionEvent event) {

    }

    @FXML private void repetitionCheckboxStateChanged(ActionEvent event) {
        if(repetitionCheckbox.isSelected()) {
            showEndDayForm();
        } else {
            removeEndDayForm();
        }
    }
    */

    public AppointmentPopupViewController(Pane calendarPane) {
        this.calendarPane = calendarPane;

    }
    /*
    private void showEndDayForm() {
        repetitionFrequencyLabel.setVisible(true);
        repetitionFrequencyTextField.setVisible(true);
        endDateLabel.setVisible(true);
        endDatePicker.setVisible(true);
    }

    private void removeEndDayForm() {
        repetitionFrequencyLabel.setVisible(false);
        repetitionFrequencyTextField.setVisible(false);
        endDateLabel.setVisible(false);
        endDatePicker.setVisible(false);
    }
    */

    public void show(DayView pane,LocalDateTime startDate, LocalDateTime endDate){

        try {
            // Init popupview from FXML
            FXMLLoader testLoader = new FXMLLoader(getClass().getResource("../views/AppointmentPopupView.fxml"));
            Pane appointmentPopup = testLoader.load();
            appointmentPopup.setId("appointmentPopup");

            // Get controller, add view to main view
            //AppointmentPopupViewController appointmentPopupViewController = testLoader.getController();
            calendarPane.getChildren().add(appointmentPopup);
            openAppointmentPopups.add(appointmentPopup);

            // Set popup to center position FIX!
            double appointmentPopupWidth = appointmentPopup.getWidth();
            double appointmentPopupHeight = appointmentPopup.getHeight();
            double mainPaneWidth = calendarPane.getLayoutX();
            double mainPaneHeight = calendarPane.getLayoutY();
            appointmentPopup.setLayoutX(mainPaneWidth/2 - appointmentPopupWidth/2);
            appointmentPopup.setLayoutY(mainPaneHeight/2 - appointmentPopupHeight/2);

            appointmentPopup.setLayoutX(80);
            appointmentPopup.setLayoutY(100);

            //Set methods
            Button closeButton = (Button) appointmentPopup.lookup("#closeButton");
            Button saveButton = (Button) appointmentPopup.lookup("#saveButton");



            startTime       = (TextField) appointmentPopup.lookup("#startTime");
            endTime         = (TextField) appointmentPopup.lookup("#endTime");
            appointmentDate = (DatePicker) appointmentPopup.lookup("#startDatePicker");
            formaalField    = (TextArea) appointmentPopup.lookup("#purposeTextArea");
            roomField       = (TextField) appointmentPopup.lookup("#roomTextField");
            titleField      = (TextField) appointmentPopup.lookup("#titleTextField");
            dayPane         = pane;


            String startHour = Integer.toString(startDate.getHour()) + ":00";
            String endHour = Integer.toString(endDate.getHour() + 1) + ":00";
            startTime.setText(startHour);
            endTime.setText(endHour);



            appointmentDate.setValue(pane.getDate());

            closeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    close();
                }
            });

            saveButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    save();
                }
            });




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        for (int i = 0; i < calendarPane.getChildren().size(); i++) {
            if (openAppointmentPopups.contains(calendarPane.getChildren().get(i))) {
                calendarPane.getChildren().remove(calendarPane.getChildren().get(i));
            }
        }
    }

    private void save(){

        LocalDate date = ((DayView)dayPane).getDate();

  /*      int startHour = Integer.valueOf(this.startTime.getText().substring(0, 2));
        int endHour = Integer.valueOf(this.endTime.getText().substring(0,2));

        Date startTime = new Date(date.getYear(),date.getMonthValue(),date.getDayOfMonth());
        Date endTime = new Date(date.getYear(),date.getMonthValue(),date.getDayOfMonth());





        startTime.setHours(startHour);
        endTime.setHours(endHour);



        String title = titleField.getText();
        String description = formaalField.getText();
        String id = "id";
        String  personId = "personId";
        String roomId = "roomId";
        ArrayList<Person> participants = new ArrayList<>();
        Person createdBy = new Person(1, "", "", "lasse@drevland.no");




        Appointment newAppointment = new Appointment(startTime,endTime,title,description,createdBy,id,roomId,personId);
        Server.getInstance().createAppointment(newAppointment);
        */


    }
}
