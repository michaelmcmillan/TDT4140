package controllers;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.collections.ArrayChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainViewController   {

    private final String MAINVIEW_PATH = "MainView.fxml";

    public MainViewController(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/"+MAINVIEW_PATH));
        Parent main = (Parent) loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        primaryStage.setScene(new Scene(main));

        final ListView<String> calendarListView = (ListView) main.lookup("#calendarListView");
        final Pane mondayPane = (Pane) main.lookup("#dayMonday");
        final Pane tuesdayPane = (Pane) main.lookup("#dayTuesday");
        final Pane wenesdayPane = (Pane) main.lookup("#dayWednesday");
        final Pane thursdayPane = (Pane) main.lookup("#dayThursday");
        final Pane fridayPane = (Pane) main.lookup("#dayFriday");
        final Pane saturdayPane = (Pane) main.lookup("#daySaturday");
        final Pane sundayPane = (Pane) main.lookup("#daySunday");

        Pane[] dayPanes = {mondayPane,tuesdayPane,wenesdayPane,thursdayPane,fridayPane,saturdayPane,sundayPane};


        //Handle clicks in calendar
        for (Pane p:dayPanes){
            p.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Pane clickedPane = (Pane) event.getSource();
                    String id = clickedPane.getId();

                    double mouseX = event.getX();

                    double mouseY = event.getY();

                    //System.out.print("Clicked: "+ id + " Mousey: "+ Double.toString(mouseY)+ System.lineSeparator());
                    calendarDayClicked(clickedPane,id,mouseY);
                }
            });
        }



        //Handle clicks in sidebar (Kalendervelger)
        ObservableList<String> list = FXCollections.observableArrayList("KAttt","Hund","hest");
        calendarListView.setItems(list);

        calendarListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.print(calendarListView.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void init(){

    }

    private void calendarDayClicked(Pane pane,String paneID, Double mouseY){
        Double height = pane.getHeight();
        double hour = (mouseY/(height/24));
        BigDecimal hourBD = BigDecimal.valueOf(hour);
        hourBD.setScale(0, BigDecimal.ROUND_DOWN);
        int hourInt = hourBD.intValue();
        System.out.print(paneID + "  :  " + Double.toString(height) + " : " + Double.toString(mouseY) + " Hour: " + Integer.toString(hourInt) +System.lineSeparator());






    }







}
