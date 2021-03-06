package controllers;

import helpers.CalendarHelper;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Appointment;
import models.Calendar;
import models.Room;
import server.Server;
import views.AppointmentView;
import views.DayView;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private HBox weekHBox;
    private double DAY_WIDTH = 111;
    private double startX, startY, endX, endY;
    private ArrayList<DayView> dayPanes = new ArrayList<DayView>();
    private Rectangle rect;
    private final double appointmentRectangleCornerRadius = 0;
    DropShadow dropShadow = new DropShadow(7,4.0,4.0,Color.BLACK);
    private java.util.Calendar startOfWeek;
    private AppointmentPopupViewController popupView;
    private boolean isDragging;
    private LocalDate firstDayOfWeek;
    private ArrayList<Room> roomArrayList = new ArrayList<>();

    DropShadow glow = new DropShadow(5,0,0,Color.RED);
    private Line line;

    public CalendarViewController(MainViewController mainViewController, Stage primarystage) {

        this.mainViewController = mainViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        this.calendarPane = (AnchorPane) this.mainScene.lookup("#calendarPane");
        this.scrollPane = (ScrollPane) this.mainScene.lookup("#weekView");
        this.weekHBox = (HBox) this.mainScene.lookup("#weekHBox");

        // Set default calendar
        popupView = new AppointmentPopupViewController(calendarPane, mainViewController);
        startOfWeek = java.util.Calendar.getInstance();
        startOfWeek.set(java.util.Calendar.DAY_OF_MONTH, 2);

        LocalDate firstDayOfWeek = CalendarHelper.getFirstDateOfWeek();
        this.generateDayPanes(firstDayOfWeek);
        this.populateWeekWithAppointments(firstDayOfWeek);
        roomArrayList.addAll(Server.getInstance().getAllRooms());
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public Pane getCurrentDayPane () {
        java.util.Calendar now = java.util.Calendar.getInstance();
        int day = now.get(now.DAY_OF_WEEK); // Sun: 1, Sat: 7
        return dayPanes.get((day % dayPanes.size()) - 2);
    }

    public void highlightCurrentDay (DayView dayView) {//#e6e1e1
        dayView.setStyle("-fx-background-color: rgba(230,225,225,0.7);");
        //dayView.setOpacity(0.85);
    }

    public void highlightCurrentHour (DayView dayView) {
        java.util.Calendar now = java.util.Calendar.getInstance();
        int hoursPassedToday = now.get(now.HOUR_OF_DAY);
        int minutesPassedThisHour = now.get(now.MINUTE);
        double dayHeight = dayPanes.get(0).getPrefHeight();
        double yPos = CalendarHelper.convertHourAndMinutesToYAxis(dayHeight, hoursPassedToday, minutesPassedThisHour);
        line = new Line(1, yPos, dayPanes.get(0).getPrefWidth() - 1, yPos);
        line.setStroke(Color.RED);
        line.setStrokeWidth(3);
        line.setEffect(glow);

        // Place the red line
        dayView.getChildren().add(line);

        // Center scroll position to current time
        this.setScrollPanePosition(yPos);
    }

    public void setScrollPanePosition(double yPos) {
        double dayHeight = dayPanes.get(0).getHeight();
        double viewHeight = scrollPane.getHeight();
        double middlePosition = viewHeight/2;
        this.scrollPane.setVvalue((yPos + middlePosition) / dayHeight);
    }

    public void addHourBreakers () {
        for (int i = 0; i < 24; i++) {
            Line hourBreaker = new Line();
            hourBreaker.setLayoutX(30);
            hourBreaker.setLayoutY(50 + (i * 50));
            hourBreaker.setStartX(-100);
            hourBreaker.setEndX(750);
            hourBreaker.setOpacity(0.1);
            dayPanes.get(0).getChildren().add(hourBreaker);
        }
    }

    public void createAppointmentViewOnMouseDrag(final DayView pane, double startY, double endY) {

        // Get start and end times based on the rectangle positioning
        int startTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.min(startY, endY));
        int endTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.max(startY, endY));

        createAppointmentView(pane, LocalDateTime.of(pane.getDate(), LocalTime.of(startTime[0], startTime[1])), LocalDateTime.of(pane.getDate(), LocalTime.of(endTime[0], endTime[1])), true);
    }

    public void createAppointmentView(final DayView pane, Appointment appointment, boolean showPopup){

        LocalDateTime startTime = appointment.getStartTime();
        LocalDateTime endTime = appointment.getEndTime();

        LocalTime dayStartTime = startTime.toLocalTime();
        LocalTime dayEndTime = endTime.toLocalTime();



        // Create the rectangle view
        final AppointmentView rectangle = new AppointmentView();
        rectangle.setX(1);
        int minY = (int) Math.min(CalendarHelper.convertLocalTimeToYAxis(pane.getPrefHeight(), dayStartTime), CalendarHelper.convertLocalTimeToYAxis(pane.getPrefHeight(), dayEndTime));
        int maxY = (int) Math.max(CalendarHelper.convertLocalTimeToYAxis(pane.getPrefHeight(), dayStartTime), CalendarHelper.convertLocalTimeToYAxis(pane.getPrefHeight(), dayEndTime));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTimeString = formatter.format(appointment.getStartTime());
        String endTimeString = formatter.format(appointment.getEndTime());



        //adds title to rectangles
        String roomName ="" ;

        if (appointment.getRoomId() != 0){
            for (Room r:roomArrayList){
                if (r.getId() == appointment.getRoomId()){
                    roomName = r.getName();
                }
            }

        }

        Text detailsText = new Text(startTimeString + " - " + endTimeString + System.lineSeparator() + appointment.getTitle() + System.lineSeparator() + roomName);

        detailsText.setWrappingWidth(DAY_WIDTH);
        detailsText.setX(5);
        detailsText.setY(minY + 20);
        detailsText.setFont(Font.font("Helvetica"));
        detailsText.setFill(Color.WHITE);
        detailsText.setPickOnBounds(false);



        maxY += pane.getPrefHeight()/24;
        maxY = maxY == minY ? maxY += pane.getPrefHeight()/24 : maxY;
        rectangle.setY(minY + 1);
        rectangle.setWidth(DAY_WIDTH);
        double height = abs(maxY - minY) - 2;
        rectangle.setHeight(height);
        rectangle.setArcHeight(appointmentRectangleCornerRadius);
        rectangle.setArcWidth(appointmentRectangleCornerRadius);
        rectangle.setStroke(Color.BLACK);

        rectangle.setOpacity(0.9);
        rectangle.setEffect(dropShadow);

        if (appointment.isParticipating()){
            rectangle.setFill(Color.valueOf("#069046"));
        } else {
            rectangle.setFill(Color.valueOf("#0C85E7"));
        }


        rectangle.setDetailsText(detailsText);
        rectangle.setAppointment(appointment);

        pane.getChildren().add(rectangle);
        pane.getChildren().add(detailsText);


        rectangles.add(rectangle);

        if (showPopup)
            popupView.show(pane,appointment,false,mainViewController.getCurrentPerson());

        // Listeners
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(!isDragging){
                    Appointment a = ((AppointmentView)t.getSource()).getAppointment();
                    popupView.show(pane,a,true,mainViewController.getCurrentPerson());
                    rectangle.setClicked(true);
                }
            }
        });

        // Check collisions between this and all other rectangles (appointments)
        CalendarHelper.checkRectangleCollisions(DAY_WIDTH, rectangle, rectangles);

    }

    public void createAppointmentView(final DayView pane, LocalDateTime startTime, LocalDateTime endTime, boolean showPopup) {
        Appointment appointment = new Appointment();

        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setPersonId(mainViewController.getCurrentPerson().getId());

        createAppointmentView(pane,appointment,showPopup);


    }

    public void generateDayPanes(LocalDate firstDayOfWeek) {

        LocalDate date = firstDayOfWeek;
        this.weekHBox.getChildren().clear();
        this.dayPanes.clear();

        // Add days to HBox programmatically.
        for (int i = 0; i < 7; i++) {
            DayView dayView = new DayView();
            dayView.setPrefSize(111, 1200);
            dayView.setLayoutY(0);
            dayView.setLayoutX(111 * i);
            dayView.setStyle("-fx-border-color: #8f8f8f; -fx-border-width: 0.5px;");
            dayView.setDate(date);
            this.weekHBox.getChildren().add(dayView);
            dayPanes.add(dayView);

            // If the pane is current day, highlight it
            LocalDate d1 = dayView.getDate();
            LocalDate d2 = LocalDate.now();
            if (d1.equals(d2)) {
                this.highlightCurrentHour(dayView);
                this.highlightCurrentDay(dayView);
                line.toFront();
            }
            date = date.plusDays(1);
        }

        // Handle clicks in calendar
        for (DayView pane:dayPanes) {
            pane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    DayView clickedPane = (DayView) event.getSource();
                    DAY_WIDTH = clickedPane.getWidth() - 2;
                    String id = clickedPane.getId();
                    startX = event.getX();
                    startY = event.getY();
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

                    final DayView clickedPane = (DayView) event.getSource();
                    endX = event.getX();
                    endY = event.getY();
                    clickedPane.getChildren().remove(rect);
                    if(isDragging){
                        createAppointmentViewOnMouseDrag(clickedPane, startY, endY);
                    }
                }
            });
        }

        // Hour grid must be drawn on top of day panes
        this.addHourBreakers();
    }

    public void removeRectangles() {
        rectangles.clear();}

    public void populateWeekWithAppointments(LocalDate firstDayOfWeek) {

        this.firstDayOfWeek = firstDayOfWeek;

        // Clear attribute rectangles to avoid clog
        rectangles.clear();

        LocalDate lastDayOfWeek = firstDayOfWeek.plusDays(6);

        ArrayList<Appointment> appointments = Server.getInstance().getAppointments(this.mainViewController.getCurrentlySelectedGroup().getCalendar_id(), firstDayOfWeek, lastDayOfWeek);

        for (Appointment appointment : appointments) {
            for (DayView dayView : this.dayPanes) {
                if (dayView.getDate().equals(appointment.getStartTime().toLocalDate())) {
                    this.createAppointmentView(dayView, appointment, false);
                }
            }
        }
        line.toFront();

    }

    public void refresh(){

        if (firstDayOfWeek!=null){
            generateDayPanes(firstDayOfWeek);
            populateWeekWithAppointments(firstDayOfWeek);
        }
        }


}
