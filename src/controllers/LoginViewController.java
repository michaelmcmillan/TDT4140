package controllers;

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
import models.Person;


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

        usernameField = (TextField) scene.lookup("#usernameTextfield");
        passwordField = (TextField) scene.lookup("#passwordTextfield");
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
            if (!username.equals("") && !password.equals("")){
                // Add a new appointment to the calendar based on input times
                Person user = new Person(username);
//         final Appointment appointment = new Appointment(startDate, endDate, "Yolo", "Some awesome stuff is happening here", morten);
//        calendar.addAppointment(appointment);

                if (true){ //TODO implement check against database
                    new MainViewController(primaryStage, user);
                }
            } else {

                if (username.equals("")){
                    usernameField.setStyle("-fx-background-color: red");
                } else {
                    usernameField.setStyle("-fx-background-color: white");
                }

                if (password.equals("")){
                    passwordField.setStyle("-fx-background-color: red");
                } else {
                    usernameField.setStyle("-fx-background-color: white");
                }

            }



        } catch (Exception e){

        }



    }

    public void fireRegisterButton(ActionEvent event){

    };
}