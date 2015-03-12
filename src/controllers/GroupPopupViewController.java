package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import models.Appointment;
import models.Person;

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
    private Scene scene;
    private ListView allPersonsList;
    private ListView groupMembersList;

    public GroupPopupViewController(Pane calendarPane, MainViewController mainViewController, Stage primarystage){
        this.calendarPane = calendarPane;
        this.scene = calendarPane.getScene();
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
            Button addButton = (Button) groupPopup.lookup("#addButton");
            Button removeButton = (Button) groupPopup.lookup("#removeButton");
            TextField startTime = (TextField) groupPopup.lookup("#startTime");
            TextField endTime = (TextField) groupPopup.lookup("#endTime");
            DatePicker appointmentDate = (DatePicker) groupPopup.lookup("#startDatePicker");


            // Fill lists
            allPersonsList = (ListView) groupPopup.lookup("#allPersonsList");
            groupMembersList = (ListView) groupPopup.lookup("#groupMembersList");

            Person tempPerson = new Person("Morten", "Kleveland", "yolo@gmail.com", "passord");
            Person tempPerson2 = new Person("Marit", "Kleveland", "yolo@gmail.com", "passord");
            Person tempPerson3 = new Person("Svetlana", "Kleveland", "yolo@gmail.com", "passord");
            Person tempPerson4 = new Person("Kong", "Kleveland", "yolo@gmail.com", "passord");

            ArrayList<Person> persons = new ArrayList<>();
            persons.add(tempPerson);
            persons.add(tempPerson2);
            persons.add(tempPerson3);
            persons.add(tempPerson4);

            ObservableList<Person> personsObservableList = FXCollections.observableArrayList();
            ObservableList<Person> groupMembersObservableList = FXCollections.observableArrayList();

            // If debug is disabled, get group data from server
            for (Person person : persons) {
                //personsObservableList.add(person.getFirstName() + " " + person.getSurname());
                personsObservableList.add(person);
            }

            allPersonsList.setItems(personsObservableList);
            groupMembersList.setItems(groupMembersObservableList);


            allPersonsList.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
                @Override
                public ListCell<Person> call(ListView<Person> param) {
                    ListCell<Person> cell = new ListCell<Person>() {
                        @Override
                        protected void updateItem(Person p, boolean bln) {
                            super.updateItem(p, bln);
                            if (p != null) {
                                setText(p.getFirstName());
                            }
                        }
                    };
                    return cell;
                }
            });

            groupMembersList.setCellFactory(new Callback<ListView<Person>, ListCell<Person>>() {
                @Override
                public ListCell<Person> call(ListView<Person> param) {
                    ListCell<Person> cell = new ListCell<Person>() {
                        @Override
                        protected void updateItem(Person p, boolean bln) {
                            super.updateItem(p, bln);
                            if (p != null) {
                                setText(p.getFirstName());
                            }
                        }
                    };
                    return cell;
                }
            });


            // Listeners
            addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Person selectedPerson = (Person)allPersonsList.getSelectionModel().getSelectedItem();
                    if (!groupMembersObservableList.contains(selectedPerson)) {
                        groupMembersObservableList.add(selectedPerson);
                        groupMembersList.setItems(groupMembersObservableList);
                    }
                }
            });

            removeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Person selectedPerson = (Person)groupMembersList.getSelectionModel().getSelectedItem();
                    if (groupMembersObservableList.contains(selectedPerson)) {
                        groupMembersObservableList.remove(selectedPerson);
                        groupMembersList.setItems(groupMembersObservableList);
                    }
                }
            });

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
