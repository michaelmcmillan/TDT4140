package controllers;

import com.sun.deploy.config.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Appointment;
import models.Calendar;
import models.Person;
import server.Server;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


public class LoginViewController{
    private Stage primaryStage;
    private Scene scene;
    private final String LOGINVIEW_PATH = "../views/LoginView.fxml";
    TextField usernameField;
    TextField passwordField;
    Button loginButton;
    Button registerButton;

    public LoginViewController(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGINVIEW_PATH));
        Parent root = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Login - Kalendersystem");
        scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setResizable(false);
        primaryStage.show();

        this. usernameField = (TextField) scene.lookup("#usernameTextfield");
        this.passwordField = (TextField) scene.lookup("#passwordTextfield");

        /* Debugging */
        this.usernameField.setText("jonaslaksen@live.com");
        this.passwordField.setText("heisann");

        loginButton = (Button) scene.lookup("#loginButton");
        registerButton = (Button) scene.lookup("#registerButton");

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {

                    doLogin();

                }

            }
        });

        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                doLogin();
            }
        });



    }


    private final String MAINVIEW_PATH = "MainView.fxml";

    public void doLogin() {

        try {

            String username = usernameField.getText();
            String password = passwordField.getText();


            // Don't use the server to login if debug is enabled
            if (application.Config.getInstance().DEBUG == true) {
                Person user = new Person(0, "Debug", "Debug", "Debug");
                new MainViewController(primaryStage, user);

            // Authenticate with the server if debug is disabled
            } else {
                Server.getInstance().logInAs(username, password);

                if (Server.getInstance().isAuthenticated()) {
                    Person user = Server.getInstance().getCurrentlyLoggedInPerson();
                    new MainViewController(primaryStage, user);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void fireRegisterButton(ActionEvent event){

    };
}