package application;

import controllers.LoginViewController;
import javafx.application.Application;
import javafx.stage.Stage;
import server.Server;

public class Main extends Application {
    private final String LOGINVIEW_PATH = "\"../views/LoginView.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        new LoginViewController(primaryStage);
    }

    public static void main(String[] args) {



        launch(args);
    }
}
