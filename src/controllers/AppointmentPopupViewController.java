package controllers;

import javafx.event.EventHandler;
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
import java.util.ResourceBundle;


public class AppointmentPopupViewController  implements Initializable {

    private MainViewController mainview;
    private ArrayList<Pane> openAppointmentPopups = new ArrayList<Pane>();
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    private Appointment model;
    private Pane calendarPane;
    private Pane dayPane;
    TextField   startTime;
    TextField   endTime;
    TextField titleField;
    TextArea descriptionField;
    TextField roomField;
    DatePicker  appointmentDate;
    Button closeButton;
    Button saveButton ;
    Button deleteButton;
    CheckBox participatingCheckBox;
    Label userLabel;
    private boolean editExistingAppointment;
    private Appointment currentAppointment;
    private ArrayList<Person> allUsers = new ArrayList<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //model = new Appointment();
        //removeEndDayForm(); // Don't show calendar repetition events at startup
    }


    public AppointmentPopupViewController(Pane calendarPane, MainViewController mainview) {
        this.mainview = mainview;
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

    public void show(DayView pane,Appointment appointment,boolean editExistingAppointment,Person currentUser){
        this.editExistingAppointment = editExistingAppointment;
        this.currentAppointment = appointment;
        LocalDateTime startDate = appointment.getStartTime();
        LocalDateTime endDate = appointment.getEndTime();
        Boolean userCanEdit = currentUser.getId() == appointment.getPersonId();

        allUsers.addAll(Server.getInstance().getAllUsers());




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
            closeButton = (Button) appointmentPopup.lookup("#closeButton");
            saveButton = (Button) appointmentPopup.lookup("#saveButton");
            deleteButton = (Button) appointmentPopup.lookup("#deleteButton");

            if (editExistingAppointment)
                saveButton.setText("Lagre");



            startTime       = (TextField) appointmentPopup.lookup("#startTime");
            endTime         = (TextField) appointmentPopup.lookup("#endTime");
            appointmentDate = (DatePicker) appointmentPopup.lookup("#startDatePicker");
            descriptionField = (TextArea) appointmentPopup.lookup("#purposeTextArea");
            roomField       = (TextField) appointmentPopup.lookup("#roomTextField");
            titleField      = (TextField) appointmentPopup.lookup("#titleTextField");
            userLabel       = (Label) appointmentPopup.lookup("#userLabel");
            dayPane         = pane;

            if (!userCanEdit){
                startTime.setEditable(false);
                endTime.setEditable(false);
                appointmentDate.setEditable(false);
                descriptionField.setEditable(false);
                roomField.setEditable(false);
                titleField.setEditable(false);


                for (Person p : allUsers){
                    if (p.getId() == currentAppointment.getPersonId()){
                        String fornavn = p.getFirstName();
                        String etternavn = p.getSurname();
                        String email = p.getEmail();
                        userLabel.setText(fornavn + " " + etternavn + " ("+email+")");
                        break;
                    }
                }


            } else {
                userLabel.setText("Deg");
                deleteButton.setDisable(false);
            }

            participatingCheckBox = (CheckBox) appointmentPopup.lookup("#participatingCheckBox");
            participatingCheckBox.setSelected(appointment.isParticipating());


            String startHour = Integer.toString(startDate.getHour()) + ":00";
            String endHour = Integer.toString(endDate.getHour() +1) + ":00";
            startTime.setText(startHour);
            endTime.setText(endHour);
            titleField.setText(appointment.getTitle());
            descriptionField.setText(appointment.getDescription());



            appointmentDate.setValue(pane.getDate());

        closeButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                close();
            }
        });

            saveButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    save(userCanEdit);

                }
            });

            deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Server.getInstance().deleteAppointment(appointment);
                    close();
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
        mainview.refresh();
    }


    private void save (Boolean userCanEdit) {

        LocalDate date = appointmentDate.getValue();

        int startHour = Integer.valueOf(this.startTime.getText().split(":")[0]);
        int endHour = Integer.valueOf(this.endTime.getText().split(":")[0]);

        LocalDateTime startTime = date.atTime(startHour, 0);
        LocalDateTime endTime = date.atTime(endHour-1, 0);

        ArrayList<Person> participants = new ArrayList<>();

        //Appointment newAppointment = new Appointment(startTime, endTime, this.titleField.getText(), this.descriptionField.getText());

        currentAppointment.setTitle(titleField.getText());
        currentAppointment.setDescription(descriptionField.getText());
        currentAppointment.setStartTime(startTime);
        currentAppointment.setEndTime(endTime);



        if (userCanEdit){
            if (editExistingAppointment){
                Server.getInstance().updateAppointment(currentAppointment);;

            } else {
                Server.getInstance().createAppointment(mainview.getCurrentlySelectedGroup().getCalendar_id(), currentAppointment);;
            }
        }

        if(participatingCheckBox.isSelected()){
            Server.getInstance().joinAppointment(currentAppointment.getId());
        } else {
            Server.getInstance().declineAppointment(currentAppointment.getId());
        }


        close();

    }
}
