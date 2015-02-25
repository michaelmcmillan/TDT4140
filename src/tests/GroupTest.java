package tests;

import models.Person;
import models.Group;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GroupTest {

    @Test
    public void testGroupArray() {
        Person person1 = new Person("Barack", "Obama");
        Person person2 = new Person("Bill", "Clinton");
        Person person3 = new Person("George", "Bush");
        Group group = new Group();

        group.addPerson(person1);
        assertNotNull(group);

        group.addPerson(person1);
        ArrayList<Person> testArray = new ArrayList<Person>();
        testArray.add(person1);
    }
}
