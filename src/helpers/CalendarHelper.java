package helpers;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.floor;

/**
 * Created by michaelmcmillan on 03.03.15.
 */
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

    public static double convertHourAndMinutesToPixels(double paneHeight, int hours, int minutes) {
        double hourHeightInPixels = paneHeight/24;
        double minutePixels = (hourHeightInPixels/60) * minutes;
        double hourPixels = hourHeightInPixels * hours;
        return hourPixels + minutePixels;
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
        }
    }

}
