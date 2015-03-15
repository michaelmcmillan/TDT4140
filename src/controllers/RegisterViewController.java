package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

    private final String REGISTERVIEW_PATH = "../views/RegisterView.fxml";
    private Stage primaryStage;
    private Button registerButton;
    private Button cancelButton;
    @FXML TextField emailField, passwordField, firstnameField, surnameField;

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

        registerButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // TODO: Make a Server.getInstance().addPerson(person) method
                // TODO: Validation.....
                /*
                int id = 123; // TODO: Overload Person constructur without ID?
                Person person = new Person(id, emailField.getText(), firstnameField.getText(), surnameField.getText());
                ArrayList persons = Server.getInstance().getAllUsers();
                if (!persons.contains(person)) {
                    Server.getInstance().addPerson(person);
                }
                */

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
