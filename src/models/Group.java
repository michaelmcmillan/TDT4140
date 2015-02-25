package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morten on 25.02.15.
 */
public class Group {

    List<Person> persons;

    public Group() {
        persons = new ArrayList<Person>();
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public void removePerson(Person person) {
        if (persons.contains(person)) {
            persons.remove(person);
        }
    }
}