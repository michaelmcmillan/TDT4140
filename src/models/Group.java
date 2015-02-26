package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morten on 25.02.15.
 */
public class Group {

    List<Person> members;
    ArrayList<Group> subGroups; //Burde det ikke vaere superGroups?
    String name;

    public Group() {

    }
}