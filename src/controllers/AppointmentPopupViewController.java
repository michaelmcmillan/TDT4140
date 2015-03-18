package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import models.Appointment;
import models.Person;
import models.Room;
import server.Server;
import views.DayView;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class AppointmentPopupViewController  implements Initializable {

    private MainViewController mainview;
    private ArrayList<Pane> openAppointmentPopups = new ArrayList<Pane>();
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    private Appointment model;
    private Pane calendarPane;
    private Pane dayPane;
    private TextField   startTime;
    private TextField   endTime;
    private TextField titleField;
    private TextArea descriptionField;
    private TextArea attendeesTextArea;
    private DatePicker  appointmentDate;
    private Button closeButton;
    private Button saveButton ;
    private Button deleteButton;
    private CheckBox participatingCheckBox;
    private Label userLabel;
    private ComboBox<Room> roomSelector;
    private boolean editingExistingAppointment;
    private ObservableList<Room> observableRoomList = FXCollections.observableArrayList();
    private Appointment currentAppointment;
    private ArrayList<Person> allUsers = new ArrayList<>();
    private boolean userCanEdit;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //model = new Appointment();
        //removeEndDayForm(); // Don't show calendar repetition events at startup
    }


    public AppointmentPopupViewController(Pane calendarPane, MainViewController mainview) {
        this.mainview = mainview;
        this.calendarPane = calendarPane;

    }


    public void show(DayView pane,Appointment appointment,boolean editExistingAppointment,Person currentUser){
        this.editingExistingAppointment = editExistingAppointment;
        this.currentAppointment = appointment;
        LocalDateTime startDate = appointment.getStartTime();
        LocalDateTime endDate = appointment.getEndTime();
        userCanEdit = currentUser.getId() == appointment.getPersonId();

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


            if (editExistingAppointment){
                 saveButton.setText("Lagre");
            }




            startTime       = (TextField) appointmentPopup.lookup("#startTime");
            endTime         = (TextField) appointmentPopup.lookup("#endTime");
            appointmentDate = (DatePicker) appointmentPopup.lookup("#startDatePicker");
            descriptionField= (TextArea) appointmentPopup.lookup("#purposeTextArea");
            attendeesTextArea = (TextArea) appointmentPopup.lookup("#attendeesTextArea");
            roomSelector    = (ComboBox) appointmentPopup.lookup("#roomComboBox");
            titleField      = (TextField) appointmentPopup.lookup("#titleTextField");
            userLabel       = (Label) appointmentPopup.lookup("#userLabel");
            dayPane         = pane;



            if (editExistingAppointment){
                ArrayList<Person> attendees = new ArrayList<>();

                attendees.addAll(Server.getInstance().getAttendees(appointment));

                for (Person p : attendees){
                    attendeesTextArea.appendText(p.getFirstName() + " " + p.getSurname() + " ("+p.getEmail()+")" + System.lineSeparator());
                }
            }



            roomSelector.setItems(observableRoomList);

            roomSelector.setCellFactory(new Callback<ListView<Room>, ListCell<Room>>() {
                @Override
                public ListCell<Room> call(ListView<Room> param) {
                    ListCell<Room> cell = new ListCell<Room>(){
                        @Override
                        protected void updateItem(Room item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!empty){
                                if (item.getId() == appointment.getRoomId()){
                                    setText("[ " +item.getName() + " ] ("+item.getCapacity()+" plasser)");
                                } else {
                                    setText(item.getName() + " ("+item.getCapacity()+" plasser)");
                                }

                            }


                        }




                    };
                    return cell;
                }


            });




            if (!userCanEdit){
                startTime.setEditable(false);
                endTime.setEditable(false);
                appointmentDate.setEditable(false);
                appointmentDate.setDisable(true);
                descriptionField.setEditable(false);
                roomSelector.setEditable(false);
                roomSelector.setDisable(true);
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


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            String startHour = formatter.format(startDate);
            String endHour = formatter.format(endDate);
            startTime.setText(startHour);
            endTime.setText(endHour);
            titleField.setText(appointment.getTitle());
            descriptionField.setText(appointment.getDescription());



            appointmentDate.setValue(pane.getDate());


            getRoomSuggestions();

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


            appointmentDate.valueProperty().addListener(e -> getRoomSuggestions());
            this.startTime.textProperty().addListener(e -> getRoomSuggestions());
            this.endTime.textProperty().addListener(e -> getRoomSuggestions());




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


        LocalDateTime startTime = parseTextToTime(this.startTime.getText());
        LocalDateTime endTime = parseTextToTime(this.endTime.getText());

        ArrayList<Person> participants = new ArrayList<>();

        //Appointment newAppointment = new Appointment(startTime, endTime, this.titleField.getText(), this.descriptionField.getText());

        currentAppointment.setTitle(titleField.getText());
        currentAppointment.setDescription(descriptionField.getText());
        currentAppointment.setStartTime(startTime);
        currentAppointment.setEndTime(endTime);
        currentAppointment.setRoomId(roomSelector.getSelectionModel().getSelectedItem().getId());




        if (userCanEdit){
            if (editingExistingAppointment){
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

    private void getRoomSuggestions(){

        LocalDateTime startTime = parseTextToTime(this.startTime.getText());
        LocalDateTime endTime = parseTextToTime(this.endTime.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String startTid = formatter.format(startTime);
        String sluttTid = formatter.format(endTime);

        ArrayList<Room> roomArrayList = new ArrayList<>();

        if(editingExistingAppointment){
            roomArrayList.addAll(Server.getInstance().getRoomSuggestions(currentAppointment,startTid,sluttTid));
        } else {
            roomArrayList.addAll(Server.getInstance().getRoomSuggestions(mainview.getCurrentlySelectedGroup(),startTid,sluttTid));
        }

        observableRoomList.clear();
        Room r = new Room(0,"Ikke reservert rom",0);
        observableRoomList.add(r);
        observableRoomList.addAll(roomArrayList);

        if (observableRoomList.size() > 0){
            if (userCanEdit){
                roomSelector.setDisable(false);

            }




        } else{
            roomSelector.setDisable(true);
            r = new Room(0,"Ingen ledige rom",0);
            observableRoomList.add(r);
        }


        roomSelector.setValue(observableRoomList.get(0));


        if(editingExistingAppointment){
            for (Room selectedRoom : observableRoomList){
                if (currentAppointment.getRoomId() == selectedRoom.getId()){
                    roomSelector.setValue(selectedRoom);
                    break;
                }
            }

        }
    }

    private LocalDateTime parseTextToTime(String text){
        LocalDate date = appointmentDate.getValue();
        int hour = Integer.valueOf(text.split(":")[0]);
        int minute = Integer.valueOf(text.split(":")[1]);

        return date.atTime(hour,minute);


    };

}
