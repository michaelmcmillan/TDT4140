package tests;

import helpers.HTTPConnection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class HTTPConnectionTest {

    @Test
    public void testHTMLConnectionWithDefaultConstructor() {
        HTTPConnection connection = null;
        assertNull(connection);

        connection = new HTTPConnection();
        assertNotNull(connection);
    }

    @Test
    public void testHTMLConnectionWithAlternativeConstructor() {
        HTTPConnection connection = null;
        assertNull(connection);

        connection = new HTTPConnection("https://www.github.com");
        assertNotNull(connection);
    }

    @Test
    public void testTwoDifferentHTMLConnections() {
        HTTPConnection connection = new HTTPConnection("https://www.github.com");
        HTTPConnection connection2 = new HTTPConnection("https://www.github.com");
        assertNotSame(connection, connection2);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test //(expected = Exception.class)
    public void testConnectionWithNonfunctionalURL() {
        HTTPConnection connection = new HTTPConnection("www.123123.koldbrann");
        exception.expect(MalformedURLException.class);

    }
}
