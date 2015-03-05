package controllers;

import helpers.CalendarHelper;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Appointment;
import models.Calendar;
import views.AppointmentView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import static java.lang.Math.abs;


public class CalendarViewController implements Initializable {

    private Calendar calendar;
    private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    private ArrayList<Pane> openAppointmentPopups = new ArrayList<Pane>();
    private MainViewController mainViewController;
    private Stage primaryStage;
    private AnchorPane calendarPane;
    private Scene mainScene;
    private double DAY_WIDTH;
    private double startX, startY, endX, endY;
    private ArrayList<Pane> dayPanes = new ArrayList<Pane>();
    private Rectangle rect;
    DropShadow dropShadow = new DropShadow(0,4.0,4.0,Color.BLACK);
    private java.util.Calendar startOfWeek;
    private AppointmentPopupViewController popupView;

    public CalendarViewController(MainViewController mainViewController, Stage primarystage) {

        this.mainViewController = mainViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        this.calendarPane = (AnchorPane) this.mainScene.lookup("#calendarPane");
        calendarPane = (AnchorPane) mainScene.lookup("#calendarPane");

        // Set default calendar
        calendar = mainViewController.getPerson().getCalendars().get(0);

        popupView = new AppointmentPopupViewController(calendarPane);

        startOfWeek = java.util.Calendar.getInstance();
        startOfWeek.set(java.util.Calendar.DAY_OF_MONTH, 2);
        
        dayPanes.addAll(Arrays.asList(new Pane[]{(Pane) mainScene.lookup("#dayMonday"),
            (Pane) mainScene.lookup("#dayTuesday"),
            (Pane) mainScene.lookup("#dayWednesday"),
            (Pane) mainScene.lookup("#dayThursday"),
            (Pane) mainScene.lookup("#dayFriday"),
            (Pane) mainScene.lookup("#daySaturday"),
            (Pane) mainScene.lookup("#daySunday")
        }));

        this.highlightCurrentHour();
        this.highlightCurrentDay();
        this.addHourBreakers();

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
                    rect = new Rectangle(1,startY,0,0);
                    clickedPane.getChildren().add(rect);
                    rect.setFill(Color.DEEPSKYBLUE);
                    rect.setOpacity(0.5);
                }
            });

            pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    endX = event.getX();
                    endY = event.getY();
                    rect.setWidth(DAY_WIDTH);
                    rect.setHeight(endY - rect.getY());

                }
            });

            pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    final Pane clickedPane = (Pane) event.getSource();
                    endX = event.getX();
                    endY = event.getY();
                    System.out.println("Released at " + endX + ", " + endY);
                    clickedPane.getChildren().remove(rect);
                    createAppointmentView(clickedPane, startX, startY, endX, endY, 4);
                }
            });
        }

    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public Pane getCurrentDayPane () {
        java.util.Calendar now = java.util.Calendar.getInstance();
        int day = now.get(now.DAY_OF_WEEK); // Sun: 1, Sat: 7
        return (Pane) dayPanes.get((day % dayPanes.size()) - 2);
    }

    public void highlightCurrentDay () {
        this.getCurrentDayPane().setStyle("-fx-background-color: #DBDBDB;");
        this.getCurrentDayPane().setOpacity(0.85);
    }

    public void highlightCurrentHour () {
        java.util.Calendar now = java.util.Calendar.getInstance();
        int hoursPassedToday = now.get(now.HOUR_OF_DAY);
        int minutesPassedThisHour = now.get(now.MINUTE);
        double dayHeight = dayPanes.get(0).getHeight();
        double yPos = CalendarHelper.convertHourAndMinutesToPixels(dayHeight, hoursPassedToday, minutesPassedThisHour);
        Line line = new Line(0, yPos, dayPanes.get(0).getWidth(), yPos);
        line.setStroke(Color.RED);
        line.setStrokeWidth(3);
        this.getCurrentDayPane().getChildren().add(line);
    }

    public void addHourBreakers () {
        for (int i = 0; i < 24; i++) {
            Line hourBreaker = new Line();
            hourBreaker.setLayoutX(30);
            hourBreaker.setLayoutY(50 + (i * 50));
            hourBreaker.setStartX(-100);
            hourBreaker.setEndX(750);
            dayPanes.get(0).getChildren().add(hourBreaker);
        }
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

        Date[] firstAndLastDayOfWeek = CalendarHelper.getFirstAndLastDayOfCurrentWeek();
        for(Appointment a : calendar.getAppointmentsBetween(firstAndLastDayOfWeek[0], firstAndLastDayOfWeek[1])) {
            System.out.println("Starting: " + a.getStartTime() + "\nEnding: " + a.getEndTime() +"\n");
        }

        // Create the rectangle view
        final AppointmentView rectangle = new AppointmentView();
        rectangle.setX(1);
        int minY = Math.min(CalendarHelper.convertYAxisToNearestHour(pane, startY), CalendarHelper.convertYAxisToNearestHour(pane, endY));
        int maxY = Math.max(CalendarHelper.convertYAxisToNearestHour(pane, startY), CalendarHelper.convertYAxisToNearestHour(pane, endY));

        maxY += pane.getHeight()/24;
        maxY = maxY == minY ? maxY += pane.getHeight()/24 : maxY;
        rectangle.setY(minY + 1);
        rectangle.setWidth(DAY_WIDTH);
        double height = abs(maxY - minY) - 2;
        rectangle.setHeight(height);
        rectangle.setArcHeight(cornerRadius);
        rectangle.setArcWidth(cornerRadius);
        rectangle.setFill(Color.DEEPSKYBLUE);
        rectangle.setOpacity(0.7);
        rectangle.setEffect(dropShadow);


        pane.getChildren().add(rectangle);
        rectangles.add(rectangle);
        popupView.show(startDate, endDate);

        // Listeners
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                popupView.show(startDate, endDate);
                rectangle.setClicked(true);
            }
        });

        // Check collisions between this and all other rectangles (appointments)
        CalendarHelper.checkRectangleCollisions(pane, rectangle, rectangles);
    }
}
