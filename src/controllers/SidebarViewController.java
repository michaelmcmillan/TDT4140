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

        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Min kalender");

        // If debug is disabled get groups from server
        if (application.Config.getInstance().DEBUG == false)
            for (Group group : Server.getInstance().getGroups())
                list.add(group.getName());

        // Handle clicks in sidebar (Kalendervelger)
        calendarListView.setItems(list);

        //Add checkboxes to list:
        //calendarListView.setCellFactory(CheckBoxListCell.forListView(callback,converter));

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
