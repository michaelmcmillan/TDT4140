package controllers;

import application.Main;
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
import models.Group;
import models.Person;
import server.Server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Morten on 02.03.15.
 */
public class GroupPopupViewController {

    private TextField titleTextField;
    private MainViewController mainViewController;
    private ArrayList<Pane> openGroupPopups = new ArrayList<Pane>();
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    private Appointment model;
    private Pane calendarPane;
    private Scene scene;
    private ListView allPersonsList;
    private ListView groupMembersList;
    private ObservableList<Person> personsObservableList       ;
    private ObservableList<Person> groupMembersObservableList  ;


    public GroupPopupViewController(Pane calendarPane, MainViewController mainViewController, Stage primarystage){
        this.calendarPane = calendarPane;
        this.scene = calendarPane.getScene();
        this.mainViewController = mainViewController;
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
            Button closeButton  = (Button) groupPopup.lookup("#closeButton");
            Button saveButton   = (Button) groupPopup.lookup("#saveButton");
            Button addButton    = (Button) groupPopup.lookup("#addButton");
            Button removeButton = (Button) groupPopup.lookup("#removeButton");

            titleTextField = (TextField) groupPopup.lookup("#titleTextField");

            TextField startTime = (TextField) groupPopup.lookup("#startTime");
            TextField endTime   = (TextField) groupPopup.lookup("#endTime");
            DatePicker appointmentDate = (DatePicker) groupPopup.lookup("#startDatePicker");



            // Fill lists
            allPersonsList = (ListView) groupPopup.lookup("#allPersonsList");
            groupMembersList = (ListView) groupPopup.lookup("#groupMembersList");





            personsObservableList        = FXCollections.observableArrayList();
            groupMembersObservableList   = FXCollections.observableArrayList();


            personsObservableList.addAll(Server.getInstance().getAllUsers());

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
                                setText(p.getEmail());
                            }else {
                                setText("");
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
                                setText(p.getEmail());
                            } else {
                                setText("");
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

                    }
                }
            });

            removeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Person selectedPerson = (Person)groupMembersList.getSelectionModel().getSelectedItem();
                    if (groupMembersObservableList.contains(selectedPerson)) {
                        groupMembersObservableList.remove(selectedPerson);

                    }
                }
            });

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

    private void close() {
        for (int i = 0; i < calendarPane.getChildren().size(); i++) {
            if (openGroupPopups.contains(calendarPane.getChildren().get(i))) {
                calendarPane.getChildren().remove(calendarPane.getChildren().get(i));
            }
        }
    }

    private void save(){

        Group newGroup = new Group(titleTextField.getText());

        newGroup = Server.getInstance().createGroup(newGroup);
        ArrayList<Person> groupMembers = new ArrayList<>();
        groupMembers.addAll(groupMembersObservableList);
        Server.getInstance().addMembersToGroup(newGroup, groupMembers);
        mainViewController.refresh();





    }


}
