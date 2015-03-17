package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    //private ObservableList<String> observableGroupList;
    private  ObservableList<Group> observableGroupList;

    public SidebarViewController (MainViewController mainViewController, CalendarViewController calendarViewController,  Stage primarystage) {

        this.mainViewController = mainViewController;
        this.calendarViewController = calendarViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        calendarListView = (ListView) mainScene.lookup("#calendarListView");
        this.groupList = Server.getInstance().getGroups();

        observableGroupList = FXCollections.observableArrayList();




       // minGruppe.setCalendar_id(Server.getInstance().getCurrentlyLoggedInPerson().getCalendarId());
        Group myCalendar = new Group("Min kalender");
        myCalendar.setCalendar_id(mainViewController.getCurrentPerson().getCalendarId());
        mainViewController.setCurrentlySelectedGroup(myCalendar);
        observableGroupList.add(myCalendar);


        ArrayList<Group> groups = new ArrayList<>();
        groups.addAll(Server.getInstance().getGroups());



        observableGroupList.addAll(sortGroups(groups));


        // Handle clicks in sidebar (Kalendervelger)
        calendarListView.setItems(observableGroupList);



        //Add checkboxes to observableGroupList:
        calendarListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {
                ListCell<Group> cell = new ListCell<Group>(){
                    @Override
                    protected void updateItem(Group item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty){
                            if (item.getSupergroup() == 0 ){
                                setText(item.getName());
                            } else {
                                setText("  +  " +item.getName());
                            }

                        } else {
                            setText("");
                        }
                    }
                };

                return cell;
            }
        });

        // Select the first calendar in the observableGroupList as default

        calendarListView.getSelectionModel().select(0);
        calendarListView.getFocusModel().focus(0);

        calendarListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //ArrayList<Group> groups = Server.getInstance().getGroups();
                Group selectedGroup = ((Group)calendarListView.getSelectionModel().getSelectedItem());
                int calenderId = selectedGroup.getCalendar_id();

                calendarViewController.removeRectangles();

                mainViewController.setCurrentlySelectedGroup(selectedGroup);
                calendarViewController.generateDayPanes(mainViewController.getFirstDayOfWeek());
                calendarViewController.populateWeekWithAppointments(mainViewController.getFirstDayOfWeek());
            }
        });
    }


    public void initialize(URL location, ResourceBundle resources) {

    }

    public void refresh(){

        observableGroupList.clear();
        observableGroupList.addAll(
                sortGroups(
                        Server.getInstance().getGroups()
                )
        );


        Group myCalendar = new Group("Min kalender");
        myCalendar.setCalendar_id(mainViewController.getCurrentPerson().getCalendarId());
        observableGroupList.add(myCalendar);


    }

    private ArrayList<Group> sortGroups(ArrayList<Group> groups){


        ArrayList<Group> sortedGroups = new ArrayList<>();
        for (Group g : groups){
            if (g.getSupergroup() == 0){
                sortedGroups.add(g);

                for (Group subG :groups){
                    if (g.getId() == subG.getSupergroup()){
                        sortedGroups.add(subG);

                    }
                }

            }
        }

        return sortedGroups;
    };
}
