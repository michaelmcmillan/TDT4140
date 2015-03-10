package tests;

import helpers.CalendarHelper;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class CalendarHelperTest {

    @Test
    public void testConvertHourAndMinutesToYAxis() {
        double dayPaneHeight = 2400;
        int hours = 12;
        int minutes = 00;
        double expectedResultInPixels = 1200;
        assertEquals(expectedResultInPixels, CalendarHelper.convertHourAndMinutesToYAxis(dayPaneHeight, hours, minutes), 0.01);
    }

    @Test
    public void testConvertLocalTimeToYAxis() {
        LocalTime time = LocalTime.of(06, 00);
        double dayPaneHeight = 2400;
        double expectedResultInPixels = 600;
        assertEquals(expectedResultInPixels, CalendarHelper.convertLocalTimeToYAxis(dayPaneHeight, time), 0.01);
    }

    @Test
    public void testConvertLocalTimeToYAxisAlmostEndOfDay() {
        LocalTime time = LocalTime.of(23, 59, 59);
        double dayPaneHeight = 2400;
        double expectedResultInPixels = 2400;
        assertEquals(expectedResultInPixels, CalendarHelper.convertLocalTimeToYAxis(dayPaneHeight, time), 0.01);
    }

    @Test
    public void testConvertLocalTimeToYAxisEndOfDay() {
        LocalTime time = LocalTime.of(00, 00, 00);
        double dayPaneHeight = 2400;
        double expectedResultInPixels = 0;
        assertEquals(expectedResultInPixels, CalendarHelper.convertLocalTimeToYAxis(dayPaneHeight, time), 0.01);
    }
}
