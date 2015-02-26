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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.min;

public class MainViewController {

    private final String MAINVIEW_PATH = "MainView.fxml";
    private double DAY_WIDTH;
    double startX;
    double startY;
    double endX;
    double endY;
    AnchorPane fxmlPane;
    ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    Calendar calendar = new Calendar();

    public MainViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/"+MAINVIEW_PATH));
        Parent main = (Parent) loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        primaryStage.setScene(new Scene(main));

        final ListView<String> calendarListView = (ListView) main.lookup("#calendarListView");
        final Pane mondayPane = (Pane) main.lookup("#dayMonday");
        final Pane tuesdayPane = (Pane) main.lookup("#dayTuesday");
        final Pane wednesdayPane = (Pane) main.lookup("#dayWednesday");
        final Pane thursdayPane = (Pane) main.lookup("#dayThursday");
        final Pane fridayPane = (Pane) main.lookup("#dayFriday");
        final Pane saturdayPane = (Pane) main.lookup("#daySaturday");
        final Pane sundayPane = (Pane) main.lookup("#daySunday");

        Pane[] dayPanes = {mondayPane, tuesdayPane, wednesdayPane, thursdayPane, fridayPane, saturdayPane, sundayPane};

        //Handle clicks in calendar
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
                    calendarDayClicked(clickedPane, id, startY);
                }
            });

            pane.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    final Pane clickedPane = (Pane) event.getSource();

                    endX = event.getX();
                    endY = event.getY();
                    System.out.println("Released at " + endX + ", " + endY);
                    createRectangle(clickedPane, startX, startY, endX, endY, 12);
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

    private void calendarDayClicked(Pane pane, String paneID, Double mouseY){
        //DAY_WIDTH = pane.getWidth();
        Double height = pane.getHeight();
        double hour = (mouseY/(height/24));
        BigDecimal hourBD = BigDecimal.valueOf(hour);
        hourBD.setScale(0, BigDecimal.ROUND_DOWN);
        int hourInt = hourBD.intValue();
        System.out.print(paneID + "  :  " + Double.toString(height) + " : " + Double.toString(mouseY) + " Hour: " + Integer.toString(hourInt) +System.lineSeparator());
    }

    private int[] convertYAxisToHourAndMinutes(Pane pane, double yAxis) {
        double height = pane.getHeight();
        int hour = (int)floor(yAxis / (height / 24));
        int minutes = (int)(yAxis / (height / 24) - hour);
        return new int[]{hour, minutes * 60};
    }

    private Date[] getFirstAndLastDayOfCurrentWeek() {
        Date date = new Date();
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(java.util.Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(java.util.Calendar.DAY_OF_MONTH, -dayOfWeek);

        Date weekStart = c.getTime();
        c.add(java.util.Calendar.DAY_OF_MONTH, 6);
        Date weekEnd = c.getTime();
        return new Date[]{weekStart, weekEnd};
    }

    public void createRectangle(Pane pane, double startX, double startY, double endX, double endY, double cornerRadius) {
        int startTime[] = convertYAxisToHourAndMinutes(pane, Math.min(startY, endY));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, startTime[0]);
        cal.set(java.util.Calendar.MINUTE, startTime[1]);
        cal.set(java.util.Calendar.SECOND, 0);
        Date startDate = cal.getTime();

        int endTime[] = convertYAxisToHourAndMinutes(pane, Math.max(startY, endY));
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal2.set(java.util.Calendar.HOUR_OF_DAY, endTime[0]);
        cal2.set(java.util.Calendar.MINUTE, endTime[1]);
        cal2.set(java.util.Calendar.SECOND, 0);
        Date endDate = cal2.getTime();

        Person morten = new Person("Morten", "Møkkamann");
        Appointment appointment = new Appointment(startDate, endDate, "Yolo", "Some awesome stuff is happening here", morten);
        calendar.addAppointment(appointment);

        Date[] firstAndLastDayOfWeek = getFirstAndLastDayOfCurrentWeek();
        for(Appointment a : calendar.getAppointmentsBetween(firstAndLastDayOfWeek[0], firstAndLastDayOfWeek[1])) {
            System.out.println("Starting: " + a.getStartTime() + "\nEnding: " + a.getEndTime() +"\n");
        }

        final Rectangle rectangle = new Rectangle();
        //rectangle.setX(min(startX, endX));
        rectangle.setX(1);
        rectangle.setY(min(startY, endY));
        //rectangle.setWidth(abs(endX - startX));
        rectangle.setWidth(DAY_WIDTH);
        rectangle.setHeight(abs(endY - startY));
        rectangle.setArcHeight(cornerRadius);
        rectangle.setArcWidth(cornerRadius);

        rectangle.setFill(Color.BISQUE);
        pane.getChildren().add(rectangle);

        rectangles.add(rectangle);
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                rectangle.setFill(Color.RED);
            }
        });
        checkRectangleCollisions(rectangle);
    }

    private void checkRectangleCollisions(Rectangle rectangle) {
        boolean collisionDetected = false;
        ArrayList<Rectangle> collidingRectangles = new ArrayList<Rectangle>();
        for (Rectangle otherRectangle : rectangles) {
            if (otherRectangle != rectangle) {
                Shape intersect = Shape.intersect(rectangle, otherRectangle);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    collisionDetected = true;
                    collidingRectangles.add(otherRectangle);
                }
            }
        }
        collidingRectangles.add(rectangle);

        if (collisionDetected) {
            int numCollisions = collidingRectangles.size();
            for(int i = 0; i < collidingRectangles.size(); i++) {
                Rectangle currentRectangle = collidingRectangles.get(i);
                currentRectangle.setWidth(DAY_WIDTH/numCollisions);
                currentRectangle.setX((DAY_WIDTH/numCollisions)*i);
            }
        } else {
            rectangle.setFill(Color.BISQUE);
        }
    }
}
