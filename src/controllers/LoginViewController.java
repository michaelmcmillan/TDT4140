package controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Person;


public class LoginViewController{
    private final String MAINVIEW_PATH = "MainView.fxml";

    public void fireLoginButton(ActionEvent event) throws  Exception{
        Scene scene = ((Node)event.getTarget()).getScene();
        Stage primaryStage = (Stage) scene.getWindow();

        TextField usernameField = (TextField) scene.lookup("#usernameTextfield");
        TextField passwordField = (TextField) scene.lookup("#passwordTextfield");

        String username = usernameField.getText();
        String password = passwordField.getText();

        // Add a new appointment to the calendar based on input times
        Person user = new Person(username);
//        final Appointment appointment = new Appointment(startDate, endDate, "Yolo", "Some awesome stuff is happening here", morten);
//        calendar.addAppointment(appointment);

        if (true){ //TODO implement check against database
            new MainViewController(primaryStage, user);
        }
    }

    public void fireRegisterButton(ActionEvent event){

    };
}