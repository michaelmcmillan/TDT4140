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

    public Group(String name) {
        this.name = name;
        members = new ArrayList<Person>();
        subGroups = new ArrayList<Group>();
    }

    public void addMember(Person person){
        members.add(person);
    }

    public void addSubGroup(Group group){
        subGroups.add(group);
    }

    public void removeMember(Person person){
        members.remove(person);
    }

    public void changeName(String name){
        this.name = name;
    }
}