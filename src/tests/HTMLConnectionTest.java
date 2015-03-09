package tests;

import helpers.HTMLConnection;
import org.junit.*;
import static org.junit.Assert.*;


public class HTMLConnectionTest {

    @Test
    public void testHTMLConnection() {
        HTMLConnection connection = new HTMLConnection();
        assertNull(connection);
    }
}
