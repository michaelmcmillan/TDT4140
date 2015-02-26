package models;

import java.util.Date;

/**
 * Created by sharklaks on 26/02/15.
 */
public class Appointment {

    Date startTime;
    Date endTime;
    String title;
    String description;
    Person[] participants;
    Person createdBy;

    public Appointment () {

    }
}
