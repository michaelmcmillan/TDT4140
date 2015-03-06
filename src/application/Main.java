package application;

import controllers.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final String LOGINVIEW_PATH = "\"../views/LoginView.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        new LoginViewController(primaryStage);
        /*
        Parent root = FXMLLoader.load(getClass().getResource("../views/" + LOGINVIEW_PATH));
        primaryStage.setTitle("Login - Kalendersystem");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);
        */
    }

    public static void main(String[] args) {
        launch(args);
    }
}