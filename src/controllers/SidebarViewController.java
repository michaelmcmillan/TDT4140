package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Calendar;
import models.Person;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SidebarViewController implements Initializable {

    private ListView calendarListView;
    private MainViewController mainViewController;
    private Stage primaryStage;
    private Scene mainScene;

    public SidebarViewController (MainViewController mainViewController, Stage primarystage) {

        this.mainViewController = mainViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        calendarListView = (ListView) mainScene.lookup("#calendarListView");

        Person p = mainViewController.getPerson();
        Calendar fiskekalender = new Calendar("Fisken min");
        p.addCalendar(fiskekalender);
        p.addCalendar(new Calendar("Fisk"));
        p.addCalendar(new Calendar("Apekatt"));
        p.addCalendar(new Calendar("Annen katt"));
        p.addCalendar(new Calendar("Flygefisk"));
        p.addCalendar(new Calendar("Elefant"));
        p.addCalendar(new Calendar("Personal stuff"));
        ArrayList<Calendar> calendars = p.getCalendars();

        // Handle clicks in sidebar (Kalendervelger)
        ObservableList<String> list = FXCollections.observableArrayList(p.getCalendarNames());
        calendarListView.setItems(list);

        // Select the first calendar in the list as default
        calendarListView.getSelectionModel().select(0);
        calendarListView.getFocusModel().focus(0);

        calendarListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.print(calendarListView.getSelectionModel().getSelectedItem());
            }
        });

    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
