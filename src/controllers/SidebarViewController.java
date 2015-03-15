package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Group;
import server.Server;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SidebarViewController implements Initializable {

    private ListView calendarListView;
    private MainViewController mainViewController;
    private CalendarViewController calendarViewController;
    private Stage primaryStage;
    private Scene mainScene;
    private ArrayList<Group> groupList = new ArrayList<>();
    private ObservableList<String> observableGroupList;

    public SidebarViewController (MainViewController mainViewController, CalendarViewController calendarViewController,  Stage primarystage) {

        this.mainViewController = mainViewController;
        this.calendarViewController = calendarViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        calendarListView = (ListView) mainScene.lookup("#calendarListView");
        this.groupList = Server.getInstance().getGroups();

        observableGroupList = FXCollections.observableArrayList();
        observableGroupList.add("Min kalender");

        // If debug is disabled get groups from server
        if (application.Config.getInstance().DEBUG == false)
            for (Group group : this.groupList)
                observableGroupList.add(group.getName());

        // Handle clicks in sidebar (Kalendervelger)
        calendarListView.setItems(observableGroupList);

        //Add checkboxes to observableGroupList:
        //calendarListView.setCellFactory(CheckBoxListCell.forListView(callback,converter));

        // Select the first calendar in the observableGroupList as default
        calendarListView.getSelectionModel().select(0);
        calendarListView.getFocusModel().focus(0);

        calendarListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ArrayList<Group> groups = Server.getInstance().getGroups();
                int selectedIndex = calendarListView.getSelectionModel().getSelectedIndex();
                int calenderId;
                if (selectedIndex != 0){
                    calenderId = groups.get(selectedIndex-1).getCalendar_id();
                } else {
                    calenderId = mainViewController.getCurrentPerson().getCalendarId();
                }

                System.out.println(calenderId);


                calendarViewController.removeRectangles();
                mainViewController.setcurrentlySelectedCalendarId(calenderId);
                calendarViewController.generateDayPanes(mainViewController.getFirstDayOfWeek());
                calendarViewController.populateWeekWithAppointments(mainViewController.getFirstDayOfWeek());
            }
        });
    }


    public void initialize(URL location, ResourceBundle resources) {

    }

    public void refresh(){
        this.groupList = Server.getInstance().getGroups();
        observableGroupList.clear();
        observableGroupList.add("Min kalender");
        for (Group group : this.groupList)
            observableGroupList.add(group.getName());

    }
}
