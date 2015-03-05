package tests;

import models.Person;
import models.Group;
import org.junit.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GroupTest {

    @Test
    public void testGroupArray() {
        Person person1 = new Person("Barack");
        Person person2 = new Person("Bill");
        Person person3 = new Person("George");
        Group group = new Group("haxk");

        group.addMember(person1);
        assertNotNull(group);

        group.addMember(person1);
        ArrayList<Person> testArray = new ArrayList<Person>();
        testArray.add(person1);
    }
}
