package controllers;

import helpers.CalendarHelper;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Appointment;
import models.Calendar;
import models.Person;
import views.AppointmentView;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.lang.Math.abs;


public class CalendarViewController implements Initializable {

    private Calendar calendar = new Calendar();
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    private ArrayList<Pane> openAppointmentPopups = new ArrayList<Pane>();
    private MainViewController mainViewController;
    private Stage primaryStage;
    private AnchorPane calendarPane;
    private Scene mainScene;
    private double DAY_WIDTH;
    private double startX, startY, endX, endY;
    private ArrayList<Pane> dayPanes = new ArrayList<Pane>();

    private java.util.Calendar startOfWeek;

    private AppointmentPopupViewController popupView;

    public CalendarViewController(MainViewController mainViewController, Stage primarystage) {

        this.mainViewController = mainViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        this.calendarPane = (AnchorPane) this.mainScene.lookup("#calendarPane");

        popupView = new AppointmentPopupViewController(calendarPane);

        startOfWeek = java.util.Calendar.getInstance();
        startOfWeek.set(java.util.Calendar.DAY_OF_MONTH, 2);



        calendarPane = (AnchorPane) mainScene.lookup("#calendarPane");
        final Pane mondayPane = (Pane) mainScene.lookup("#dayMonday");
        final Pane tuesdayPane = (Pane) mainScene.lookup("#dayTuesday");
        final Pane wednesdayPane = (Pane) mainScene.lookup("#dayWednesday");
        final Pane thursdayPane = (Pane) mainScene.lookup("#dayThursday");
        final Pane fridayPane = (Pane) mainScene.lookup("#dayFriday");
        final Pane saturdayPane = (Pane) mainScene.lookup("#daySaturday");
        final Pane sundayPane = (Pane) mainScene.lookup("#daySunday");

       dayPanes.addAll(Arrays.asList(new Pane[]{mondayPane, tuesdayPane, wednesdayPane, thursdayPane, fridayPane, saturdayPane, sundayPane}));

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
                    popupView.close();
                }
            });

            pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    final Pane clickedPane = (Pane) event.getSource();
                    endX = event.getX();
                    endY = event.getY();
                    System.out.println("Released at " + endX + ", " + endY);


                    createAppointmentView(clickedPane, startX, startY, endX, endY, 12);
                }
            });
        }

    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void createAppointmentView(final Pane pane, double startX, double startY, double endX, double endY, final double cornerRadius) {

        //Get date from weekstart
        int startDay = startOfWeek.get(java.util.Calendar.DAY_OF_MONTH);

        startDay += dayPanes.indexOf(pane);


        // Get start and end times based on the rectangle positioning
        int startTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.min(startY, endY));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH,startDay);
        cal.set(java.util.Calendar.HOUR_OF_DAY, startTime[0]);
        cal.set(java.util.Calendar.MINUTE, startTime[1]);
        cal.set(java.util.Calendar.SECOND, 0);
        final Date startDate = cal.getTime();

        int endTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.max(startY, endY));
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.set(java.util.Calendar.HOUR_OF_DAY, endTime[0]);
        cal2.set(java.util.Calendar.MINUTE, endTime[1]);
        cal2.set(java.util.Calendar.SECOND, 0);
        final Date endDate = cal2.getTime();

        // Add a new appointment to the calendar based on input times
        Person morten = new Person("Morten", "Møkkamann");
        final Appointment appointment = new Appointment(startDate, endDate, "Yolo", "Some awesome stuff is happening here", morten);
        calendar.addAppointment(appointment);

        Date[] firstAndLastDayOfWeek = CalendarHelper.getFirstAndLastDayOfCurrentWeek();
        for(Appointment a : calendar.getAppointmentsBetween(firstAndLastDayOfWeek[0], firstAndLastDayOfWeek[1])) {
            System.out.println("Starting: " + a.getStartTime() + "\nEnding: " + a.getEndTime() +"\n");
        }

        // Create the rectangle view
        final AppointmentView rectangle = new AppointmentView();
        rectangle.setX(1);
        int minY = Math.min(CalendarHelper.convertYAxisToNearestHour(pane, startY), CalendarHelper.convertYAxisToNearestHour(pane, endY));
        rectangle.setY(minY);
        rectangle.setWidth(DAY_WIDTH);
        rectangle.setHeight(abs(endY - startY));
        rectangle.setArcHeight(cornerRadius);
        rectangle.setArcWidth(cornerRadius);
        rectangle.setFill(Color.BISQUE);

        pane.getChildren().add(rectangle);
        rectangles.add(rectangle);

        popupView.show(startDate,endDate);

        // Listeners
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                popupView.show(startDate,endDate);
                rectangle.setClicked(true);

            }
        });

        // Check collisions between this and all other rectangles (appointments)
        CalendarHelper.checkRectangleCollisions(pane, rectangle, rectangles);
    }





}
