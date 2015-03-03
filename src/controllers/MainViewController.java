package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Calendar;

import static java.lang.Math.abs;

public class MainViewController {

    private final ListView<String> calendarListView;
    private final String MAINVIEW_PATH = "../views/MainView.fxml";
    private CalendarViewController appointmentView;

    public MainViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        calendarListView = (ListView) main.lookup("#calendarListView");

        // Handle clicks in sidebar (Kalendervelger)
        ObservableList<String> list = FXCollections.observableArrayList("KAttt","Hund","hest");
        calendarListView.setItems(list);

        calendarListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.print(calendarListView.getSelectionModel().getSelectedItem());
            }
        });
    }

}
