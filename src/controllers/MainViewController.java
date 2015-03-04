package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainViewController {

    private final String MAINVIEW_PATH = "../views/MainView.fxml";

    private CalendarViewController appointmentView;
    private SidebarViewController sidebarView;


    public MainViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        appointmentView = new CalendarViewController(this, primaryStage);
        sidebarView = new SidebarViewController(this, primaryStage);

    }
}
