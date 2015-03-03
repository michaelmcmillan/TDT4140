package controllers;

import helpers.CalendarHelper;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

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

    public CalendarViewController(MainViewController mainViewController, Stage primarystage) {

        this.mainViewController = mainViewController;
        this.primaryStage = primarystage;
        this.mainScene = primarystage.getScene();
        this.calendarPane = (AnchorPane) this.mainScene.lookup("#calendarPane");

        calendarPane = (AnchorPane) mainScene.lookup("#calendarPane");
        final Pane mondayPane = (Pane) mainScene.lookup("#dayMonday");
        final Pane tuesdayPane = (Pane) mainScene.lookup("#dayTuesday");
        final Pane wednesdayPane = (Pane) mainScene.lookup("#dayWednesday");
        final Pane thursdayPane = (Pane) mainScene.lookup("#dayThursday");
        final Pane fridayPane = (Pane) mainScene.lookup("#dayFriday");
        final Pane saturdayPane = (Pane) mainScene.lookup("#daySaturday");
        final Pane sundayPane = (Pane) mainScene.lookup("#daySunday");

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
                    closeAppointmentPopup();
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

        // Get start and end times based on the rectangle positioning
        int startTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.min(startY, endY));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, startTime[0]);
        cal.set(java.util.Calendar.MINUTE, startTime[1]);
        cal.set(java.util.Calendar.SECOND, 0);
        Date startDate = cal.getTime();

        int endTime[] = CalendarHelper.convertYAxisToHourAndMinutes(pane, Math.max(startY, endY));
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.set(java.util.Calendar.HOUR_OF_DAY, endTime[0]);
        cal2.set(java.util.Calendar.MINUTE, endTime[1]);
        cal2.set(java.util.Calendar.SECOND, 0);
        Date endDate = cal2.getTime();

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

        // Listeners
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (rectangle.isClicked()) {
                    closeAppointmentPopup();
                    rectangle.setClicked(false);
                } else {
                    try {
                        // Init popupview from FXML
                        FXMLLoader testLoader = new FXMLLoader(getClass().getResource("../views/AppointmentPopupView.fxml"));
                        Pane appointmentPopup = testLoader.load();
                        appointmentPopup.setId("appointmentPopup");

                        // Get controller, add view to main view
                        AppointmentPopupViewController appointmentPopupViewController = testLoader.getController();
                        calendarPane.getChildren().add(appointmentPopup);
                        openAppointmentPopups.add(appointmentPopup);

                        appointmentPopup.setLayoutX(80);
                        appointmentPopup.setLayoutY(100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    rectangle.setClicked(true);
                }
            }
        });

        // Check collisions between this and all other rectangles (appointments)
        CalendarHelper.checkRectangleCollisions(pane, rectangle, rectangles);
    }


    public void closeAppointmentPopup() {
        for (int i = 0; i < calendarPane.getChildren().size(); i++) {
            if (openAppointmentPopups.contains(calendarPane.getChildren().get(i))) {
                calendarPane.getChildren().remove(calendarPane.getChildren().get(i));
            }
        }
    }
}
