package controllers;

import helpers.CalendarHelper;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private ScrollPane scrollPane;
    private double DAY_WIDTH;
    private double startX, startY, endX, endY;
    private ArrayList<Pane> dayPanes = new ArrayList<Pane>();
    private Rectangle rect;
    private final double appointmentRectangleCornerRadius = 4;
    DropShadow dropShadow = new DropShadow(0,4.0,4.0,Color.BLACK);
    private java.util.Calendar startOfWeek;
    private AppointmentPopupViewController popupView;
    private boolean isDragging;
    private Line line;

    public CalendarViewController(MainViewController mainViewController, Stage primarystage) {

        this.mainViewController = mainViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        this.calendarPane = (AnchorPane) this.mainScene.lookup("#calendarPane");
        this.scrollPane = (ScrollPane) this.mainScene.lookup("#weekView");
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
                    isDragging = false;
                }
            });

            pane.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    endX = event.getX();
                    endY = event.getY();
                    rect.setWidth(DAY_WIDTH);
                    rect.setHeight(endY - rect.getY());
                    isDragging = true;
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
                    if(isDragging){
                        createAppointmentViewOnMouseDrag(clickedPane, startY, endY);
                    }
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
        double yPos = CalendarHelper.convertHourAndMinutesToYAxis(dayHeight, hoursPassedToday, minutesPassedThisHour);
        line = new Line(1, yPos, dayPanes.get(0).getWidth() - 1, yPos);
        line.setStroke(Color.RED);
        line.setStrokeWidth(3);
        this.getCurrentDayPane().getChildren().add(line);

        this.setScrollPanePosition(yPos);
    }

    public void setScrollPanePosition(double yPos) {
        double dayHeight = dayPanes.get(0).getHeight();
        double viewHeight = scrollPane.getHeight();
        double middlePosition = viewHeight/2;
        this.scrollPane.setVvalue((yPos + middlePosition)/dayHeight);
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

    public void createAppointmentViewOnMouseDrag(final Pane pane, double startY, double endY) {

        //Get date from weekstart
        int startDay = startOfWeek.get(java.util.Calendar.DAY_OF_MONTH);
        startDay += dayPanes.indexOf(pane);

        // Get start and end times based on the rectangle positioning
        int startTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.min(startY, endY));
        int endTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.max(startY, endY));

        Date[] firstAndLastDayOfWeek = CalendarHelper.getFirstAndLastDayOfCurrentWeek();
        for(Appointment a : calendar.getAppointmentsBetween(firstAndLastDayOfWeek[0], firstAndLastDayOfWeek[1])) {
            System.out.println("Starting: " + a.getStartTime() + "\nEnding: " + a.getEndTime() +"\n");
        }

        LocalDate date = LocalDate.now(); // TODO: Change the date to pane date
        createAppointmentView(pane, LocalDateTime.of(date, LocalTime.of(startTime[0], startTime[1])), LocalDateTime.of(date, LocalTime.of(endTime[0], endTime[1])));
    }

    public void createAppointmentView(final Pane pane, LocalDateTime startTime, LocalDateTime endTime) {

        LocalTime dayStartTime = startTime.toLocalTime();
        LocalTime dayEndTime = endTime.toLocalTime();

        // Create the rectangle view
        final AppointmentView rectangle = new AppointmentView();
        rectangle.setX(1);
        int minY = (int) Math.min(CalendarHelper.convertLocalTimeToYAxis(pane.getHeight(), dayStartTime), CalendarHelper.convertLocalTimeToYAxis(pane.getHeight(), dayEndTime));
        int maxY = (int) Math.max(CalendarHelper.convertLocalTimeToYAxis(pane.getHeight(), dayStartTime), CalendarHelper.convertLocalTimeToYAxis(pane.getHeight(), dayEndTime));

        maxY += pane.getHeight()/24;
        maxY = maxY == minY ? maxY += pane.getHeight()/24 : maxY;
        rectangle.setY(minY + 1);
        rectangle.setWidth(DAY_WIDTH);
        double height = abs(maxY - minY) - 2;
        rectangle.setHeight(height);
        rectangle.setArcHeight(appointmentRectangleCornerRadius);
        rectangle.setArcWidth(appointmentRectangleCornerRadius);
        rectangle.setFill(Color.DEEPSKYBLUE);
        rectangle.setOpacity(0.7);
        rectangle.setEffect(dropShadow);

        pane.getChildren().add(rectangle);
        rectangles.add(rectangle);
        popupView.show(startTime, endTime);
        line.toFront();

        // Listeners
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(!isDragging){
                    popupView.show(startTime, endTime);
                    rectangle.setClicked(true);
                }
            }
        });

        // Check collisions between this and all other rectangles (appointments)
        CalendarHelper.checkRectangleCollisions(pane, rectangle, rectangles);
    }
}
