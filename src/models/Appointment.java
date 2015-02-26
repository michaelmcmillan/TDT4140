package models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharklaks on 26/02/15.
 */
public class Appointment {

    Date startTime;
    Date endTime;
    String title;
    String description;
    ArrayList<Person> participants;
    Person createdBy;

    //For testing
    public Appointment(){

    }

    public Appointment (Date startTime, Date endTime, String title, String description, Person createdBy) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;

        participants = new ArrayList<Person>();
        participants.add(createdBy);

    }

    public Date getStartTime(){ return this.startTime }
    public Date getEndTime(){ return this.endTime }


    public void addParticipant(Person person){
        participants.add(person);
    }

    public void removeParticipant(Person person){
        participants.remove(person);
    }

    public void changeTime(Date startTime, Date endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeDescription(String description){
        this.description = description;
    }

    public void heisann(){

    }
}
