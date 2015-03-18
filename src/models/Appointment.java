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
    private int id;
    private int  personId;
    private int roomId;
    private boolean participating = true;

    public Appointment () {
        this.title = "Avtaletittel";
    }

    public Appointment (LocalDateTime startTime, LocalDateTime endTime, String title, String description,Boolean participating) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.participating = participating;
    }

    public LocalDateTime getStartTime(){ return this.startTime;}
    public void setStartTime(LocalDateTime startTime) {this.startTime = startTime;}

    public LocalDateTime getEndTime(){ return this.endTime;}

    public void setEndTime(LocalDateTime endTime) {this.endTime = endTime;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public int getId() {
        return this.id;
    }

    public void setId (int id ) {
        this.id = id;
    }

    public boolean isParticipating () {
        return this.participating;
    }

    public void setParticipating(boolean participating) {
        this.participating = participating;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonId() {
        return personId;
    }

    public int getRoomId() {
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


    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
