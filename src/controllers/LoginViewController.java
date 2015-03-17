package controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Person;
import org.json.JSONException;
import server.Server;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class LoginViewController{
    private Stage primaryStage;
    private Scene scene;
    private final String LOGINVIEW_PATH = "../views/LoginView.fxml";
    TextField usernameField;
    TextField passwordField;
    Button loginButton;
    Button registerButton;
    Label errormessage;
    Timer timer;

    public LoginViewController(Stage primaryStage){
        try {
            this.primaryStage = primaryStage;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGINVIEW_PATH));
            Parent root = loader.load();
            loader.setController(this);
            primaryStage.setTitle("Login - Kalendersystem");
            scene = new Scene(root);
            primaryStage.setScene(scene);

            primaryStage.setResizable(false);
            primaryStage.show();

            this.usernameField = (TextField) scene.lookup("#usernameTextfield");
            this.passwordField = (TextField) scene.lookup("#passwordTextfield");
            this.errormessage = (Label) scene.lookup("#errormessage");


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
        } catch (Exception e){
            e.printStackTrace();

        }

        loginButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                doLogin();
            }
        });

        registerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    new RegisterViewController(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private final String MAINVIEW_PATH = "MainView.fxml";

    public void doLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!username.isEmpty() && !password.isEmpty()) {

            try {
                // Don't use the server to login if debug is enabled
                if (application.Config.getInstance().DEBUG == true) {
                    Person user = new Person(0, "Debug", "Debug", "Debug");
                    new MainViewController(primaryStage, user);

                // Authenticate with the server if debug is disabled
                } else {
                    Server.getInstance().logInAs(username, password);

                    if (Server.getInstance().isAuthenticated()) {
                        Person user = Server.getInstance().getCurrentlyLoggedInPerson();
                        ArrayList<Person> persons = Server.getInstance().getAllUsers();
                        new MainViewController(primaryStage, user);
                    }
                }
            } //nullpointerException,
            catch (Exception e){
                showErrorMessage(3000, e.toString());
            }
        }
    }

    private void showErrorMessage(long time, String message){
        timer = new Timer();
        errormessage.setText(message);
        errormessage.setVisible(true);
        timer.schedule(new errorTimer(),time);
    }

    class errorTimer extends TimerTask{
        public void run() {
            Platform.runLater(new Runnable() {
                public void run() {
                    errormessage.setVisible(false);
                }
            });
            timer.cancel();
        }
    }

}
