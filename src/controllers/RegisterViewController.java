package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Person;
import server.Server;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

    private final String REGISTERVIEW_PATH = "../views/RegisterView.fxml";
    private Stage primaryStage;
    private Button registerButton;
    private Button cancelButton;
    private TextField emailField, firstnameField, surnameField;
    @FXML PasswordField passwordField;

    public RegisterViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(REGISTERVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Registrer ny bruker");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        // Get buttons and textviews
        registerButton = (Button) scene.lookup("#registerButton");
        cancelButton = (Button) scene.lookup("#cancelButton");
        emailField =  (TextField) scene.lookup("#emailField");
        firstnameField = (TextField) scene.lookup("#firstnameField");
        surnameField = (TextField) scene.lookup("#surnameField");
        passwordField = (PasswordField) scene.lookup("#passwordField");


        registerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Person person = new Person(emailField.getText(), firstnameField.getText(), surnameField.getText(), passwordField.getText());
                Server.getInstance().createPerson(person);

                // TODO: Make a Server.getInstance().addPerson(person) method
                // TODO: Validation.....

                new LoginViewController(primaryStage);
            }
        });

        cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                new LoginViewController(primaryStage);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("yolo");
    }

}
