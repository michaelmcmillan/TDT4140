package tests;

import models.Person;
import org.junit.*;
import static org.junit.Assert.*;

public class PersonTest {

    @Test
    public void testPersonName() {
        Person person = new Person("Barack", "Obama");
        assertNotNull(person);
        assertEquals(person.getFirstName(), "Barack");
        assertEquals(person.getSurname(), "Obama");
    }
}
