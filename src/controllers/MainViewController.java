package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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

public class MainViewController {

    private final String MAINVIEW_PATH = "MainView.fxml";
    private double DAY_WIDTH;
    double startX;
    double startY;
    double endX;
    double endY;
    AnchorPane fxmlPane;
    ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    Calendar calendar = new Calendar();
    Parent main;
    final AnchorPane mainPane;
    final ListView<String> calendarListView;

    public MainViewController(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/" + MAINVIEW_PATH));
        main = loader.load();
        loader.setController(this);
        primaryStage.setTitle("Kalendersystem");
        primaryStage.setScene(new Scene(main));

        calendarListView = (ListView) main.lookup("#calendarListView");
        mainPane = (AnchorPane) main.lookup("#mainPane");
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
        Double height = pane.getHeight();
        double hour = (mouseY / (height / 24));
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

    private int convertYAxisToNearestHour(Pane pane, double yAxis) {
        double hourPixels = pane.getHeight()/24;
        return (int) (Math.round(yAxis/hourPixels)*hourPixels);
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


    public void createRectangle(final Pane pane, double startX, double startY, double endX, double endY, double cornerRadius) {
        // Get start and end times based on the rectangle positioning
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

        // Add a new appointment to the calendar based on input times
        Person morten = new Person("Morten", "Møkkamann");
        Appointment appointment = new Appointment(startDate, endDate, "Yolo", "Some awesome stuff is happening here", morten);
        calendar.addAppointment(appointment);

        Date[] firstAndLastDayOfWeek = getFirstAndLastDayOfCurrentWeek();
        for(Appointment a : calendar.getAppointmentsBetween(firstAndLastDayOfWeek[0], firstAndLastDayOfWeek[1])) {
            System.out.println("Starting: " + a.getStartTime() + "\nEnding: " + a.getEndTime() +"\n");
        }

        // Create the rectangle view
        final Rectangle rectangle = new Rectangle();
        rectangle.setX(1);

        int minY = Math.min(convertYAxisToNearestHour(pane, startY), convertYAxisToNearestHour(pane, endY));
        rectangle.setY(minY);
        //rectangle.setY(min(startY, endY));
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
                rectangle.setFill(Color.RED);
                int menuHeight = 300;
                int menuWidth = 200;
                double rectangleCenterX = rectangle.getWidth()/2 + pane.getLayoutX();
                double menuY = rectangle.getY() - menuHeight + 1;
                double menuX = rectangleCenterX - menuWidth/2;
                Rectangle menu = new Rectangle(menuX, menuY, menuWidth, menuHeight);
                menu.setFill(Color.SLATEGRAY);
                mainPane.getChildren().add(menu);
                //weekPane.getChildrenUnmodifiable().add(menu);
            }
        });

//        rectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent t) {
//                rectangle.setFill(Color.BLUE);
//            }
//        });

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
                currentRectangle.setWidth(DAY_WIDTH/numCollisions - 1);
                currentRectangle.setX((DAY_WIDTH/numCollisions)*i + 1);
            }
        } else {
            rectangle.setFill(Color.BISQUE);
        }
    }
}
