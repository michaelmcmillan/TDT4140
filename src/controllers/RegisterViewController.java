package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterViewController implements Initializable {

    private final String REGISTERVIEW_PATH = "../views/RegisterView.fxml";
    private Stage primaryStage;

    public RegisterViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(REGISTERVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Registrer ny bruker");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    void fireCancel(ActionEvent event) {
        new LoginViewController(primaryStage);
    }

    void fireRegister(ActionEvent event) {
        new LoginViewController(primaryStage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("yolo");
    }

}
