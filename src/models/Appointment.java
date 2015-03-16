package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Appointment {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;
    private String id;
    private String  personId;
    private String roomId;

    public Appointment () {
        this.title = "New Appointment";


    }

    public Appointment (LocalDateTime startTime, LocalDateTime endTime, String title, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.id = id;
        this.personId = personId;
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime(){ return this.startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime(){ return this.endTime;}

    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getId() {

        return id;
    }

    public String getPersonId() {
        return personId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void changeTime(LocalDateTime startTime, LocalDateTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeDescription(String description){
        this.description = description;
    }

}
