package tests;

import helpers.HTMLConnection;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLConnectionTest {

    @Test
    public void testHTMLConnection() {
        HTMLConnection connection = null;
        assertNull(connection);

        connection = new HTMLConnection();
        assertNotNull(connection);
    }
}
