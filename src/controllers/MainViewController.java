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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import models.Appointment;
import models.Calendar;
import models.Person;
import views.AppointmentView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import static java.lang.Math.abs;
import static java.lang.Math.floor;
import helpers.CalendarHelper;

public class MainViewController {

    private double DAY_WIDTH;
    private double startX, startY, endX, endY;
    private Calendar calendar = new Calendar();
    private final AnchorPane calendarPane;
    private final ListView<String> calendarListView;
    private final String MAINVIEW_PATH = "../views/MainView.fxml";
    private AppointmentViewController appointmentView;

    public MainViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAINVIEW_PATH));
        Parent main = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        Scene scene = new Scene(main);
        scene.getStylesheets().add(this.getClass().getResource("/views/style.css").toExternalForm());
        primaryStage.setScene(scene);

        calendarListView = (ListView) main.lookup("#calendarListView");
        calendarPane = (AnchorPane) main.lookup("#calendarPane");
        final Pane mondayPane = (Pane) main.lookup("#dayMonday");
        final Pane tuesdayPane = (Pane) main.lookup("#dayTuesday");
        final Pane wednesdayPane = (Pane) main.lookup("#dayWednesday");
        final Pane thursdayPane = (Pane) main.lookup("#dayThursday");
        final Pane fridayPane = (Pane) main.lookup("#dayFriday");
        final Pane saturdayPane = (Pane) main.lookup("#daySaturday");
        final Pane sundayPane = (Pane) main.lookup("#daySunday");

         appointmentView = new AppointmentViewController(this, primaryStage);

        Pane[] dayPanes = {mondayPane, tuesdayPane, wednesdayPane, thursdayPane, fridayPane, saturdayPane, sundayPane};

        // Handle clicks in calendar
        for (Pane pane:dayPanes) {
            pane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Pane clickedPane = (Pane) event.getSource();
                    DAY_WIDTH = clickedPane.getWidth() - 2;
                    String id = clickedPane.getId();
                    startX = event.getX();
                    startY = event.getY();
                    System.out.println("Clicked at " + startX + ", " + startY);
                    appointmentView.closeAppointmentPopup();
                }
            });

            pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    final Pane clickedPane = (Pane) event.getSource();
                    endX = event.getX();
                    endY = event.getY();
                    System.out.println("Released at " + endX + ", " + endY);
                    appointmentView.createAppointmentView(clickedPane, startX, startY, endX, endY, 12);
                }
            });
        }

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

    public Calendar getCalendar () {
        return calendar;
    }


}
