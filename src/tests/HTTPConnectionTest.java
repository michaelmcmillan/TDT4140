package tests;

import helpers.HTTPConnection;
import org.junit.Test;

import static org.junit.Assert.*;

public class HTTPConnectionTest {

    @Test
    public void testHTTPConnectionWithDefaultConstructor() {
        HTTPConnection connection = null;
        assertNull(connection);

        connection = new HTTPConnection();
        assertNotNull(connection);
    }

//    @Test
//    public void testHTTPConnectionWithAlternativeConstructor() {
//        HTTPConnection connection = null;
//        assertNull(connection);
//
//        connection = new HTTPConnection("https://www.github.com");
//        assertNotNull(connection);
//    }
//
//    @Test
//    public void testHTTPConnectionWithUsernamePasswordConstructor() {
//        HTTPConnection connection = null;
//        assertNull(connection);
//
//        connection = new HTTPConnection("https://www.github.com", "morten", "katt");
//        assertNotNull(connection);
//    }
//
//    @Test
//    public void testTwoDifferentHTMLConnections() {
//        HTTPConnection connection = new HTTPConnection("https://www.github.com", "morten", "katt");
//        HTTPConnection connection2 = new HTTPConnection("https://www.github.com");
//        assertNotSame(connection, connection2);
//    }

//    @Rule
//    public ExpectedException exception = ExpectedException.none();
//
//    @Test //(expected = Exception.class)
//    public void testConnectionWithNonfunctionalURL() {
//        HTTPConnection connection = new HTTPConnection("www.123123.koldbrann");
//        exception.expect(MalformedURLException.class);
//
//    }
}
