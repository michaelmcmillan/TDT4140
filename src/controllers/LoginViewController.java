package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class LoginViewController{
    private final String MAINVIEW_PATH = "MainView.fxml";

    public void fireLoginButton(ActionEvent event) throws  Exception{
        Scene scene = ((Node)event.getTarget()).getScene();
        Stage primaryStage = (Stage) scene.getWindow();

        TextField usernameField = (TextField) scene.lookup("#usernameTextfield");
        TextField passwordField = (TextField) scene.lookup("#passwordTextfield");

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (true){ //TODO implement check against database

            new MainViewController(primaryStage);

        }

    }
    public void fireRegisterButton(ActionEvent event){

    };



}