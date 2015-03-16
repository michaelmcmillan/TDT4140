package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Appointment;
import models.Group;
import models.Person;
import server.Server;

import java.io.IOException;
import java.util.ArrayList;

public class EditGroupPopupViewController  {
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
    private Group currentGroup;

    public EditGroupPopupViewController(Pane calendarPane, MainViewController mainViewController, Stage primaryStage) {
        this.calendarPane = calendarPane;
        this.scene = calendarPane.getScene();
        this.mainViewController = mainViewController;
    }



    public void show(Group currentGroup){
        this.currentGroup = currentGroup;

        try {



            // Init popupview from FXML
            FXMLLoader testLoader = new FXMLLoader(getClass().getResource("../views/EditGroupPopupView.fxml"));
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


            titleTextField = (TextField) groupPopup.lookup("#titleTextField");
            titleTextField.setText(currentGroup.getName());







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

        currentGroup.setName(titleTextField.getText());
        Server.getInstance().updateGroup(currentGroup);
        mainViewController.refresh();





    }
}
