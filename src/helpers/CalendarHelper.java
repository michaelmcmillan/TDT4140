package helpers;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.floor;

public class CalendarHelper {

    public static int[] convertYAxisToHourAndMinutes(Pane pane, double yAxis) {
        double height = pane.getHeight();
        int hour = (int)floor(yAxis / (height / 24));
        int minutes = (int)(yAxis / (height / 24) - hour);
        return new int[]{hour, minutes * 60};
    }

    public static int convertYAxisToNearestHour(Pane pane, double yAxis) {
        double hourPixels = pane.getHeight()/24;
        return (int) (Math.floor(yAxis/hourPixels)*hourPixels);
    }

    public static double convertHourAndMinutesToYAxis(double height, int hours, int minutes) {
        double hourHeightInPixels = height/24;
        double minutePixels = (hourHeightInPixels/60) * minutes;
        double hourPixels = hourHeightInPixels * hours;
        return hourPixels + minutePixels;
    }

    public static double convertLocalTimeToYAxis(double height, LocalTime time) {
        long timeInNanos = time.toNanoOfDay();
        LocalTime day = LocalTime.of(23, 59, 59);
        long dayInNanos = day.toNanoOfDay();
        return (double)timeInNanos/dayInNanos * height;
    }

    public static Date[] getFirstAndLastDayOfCurrentWeek() {
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

//    public static LocalDate[] getFirstAndLastDayOfWeek(int year, int weekNumber) {
//
//        Calendar cal = new GregorianCalendar();
//        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//        cal.setWeekDate(year, weekNumber, 2);
//
//        int firstDay = cal.MONDAY;
//        int lastDay = cal.SUNDAY;
//
//        LocalDate date = LocalDate.ofYearDay();
//        return new Date[]{weekStart, weekEnd};
//    }

    public static void checkRectangleCollisions(Pane pane, Rectangle rectangle, ArrayList<Rectangle> rectangles) {
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
                currentRectangle.setWidth(pane.getWidth()/numCollisions - 1);
                currentRectangle.setX((pane.getWidth()/numCollisions)*i + 1);
            }
            //kollisjon fikset(ish):
            for (Rectangle rect : collidingRectangles){
                checkRectangleCollisions(pane, rect, rectangles);
            }
        }
    }

    public static int getCurrentWeekNumber() {
        LocalDate date = LocalDate.now();
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        return date.get(woy);
    }
}
