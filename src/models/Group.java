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

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public List<Person> getMembers() {return members;}
    public void addMember(Person person){members.add(person);}
    public void removeMember(Person person){members.remove(person);}

    public ArrayList<Group> getSubGroups() {return subGroups;}
    public void addSubGroup(Group group){subGroups.add(group);}

}