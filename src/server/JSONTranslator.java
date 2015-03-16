package server;

import models.Appointment;
import models.Group;
import models.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class JSONTranslator {

    /*

    public static ArrayList<Group> toGroupArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0 ; i <jsonArray.length() ; i ++){
            groups.add(toGroup(jsonArray.getJSONObject(i)));
        }
        return groups;
    }



    public static ArrayList<Appointment> toAppointmentArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<Appointment> appointments = new ArrayList<>();
        for (int i = 0 ; i <jsonArray.length() ; i ++){
            appointments.add(toAppointment(jsonArray.getJSONObject(i)));
        }
        return appointments;
    }







    public static JSONObject toJSON(Person person) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", person.getId());
        jsonObject.put("email", person.getEmail());
        jsonObject.put("firstname", person.getFirstName());
        jsonObject.put("surname", person.getSurname());
        jsonObject.put("password", person.getPassword());
        jsonObject.put("alarm_time", person.getAlarmTime());
        jsonObject.put("Calendar_id", person.getCalendarId());
        return jsonObject;
    }

    public static JSONObject toJSON(Group group) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", group.getId());
        jsonObject.put("name", group.getName());
        jsonObject.put("Calendar_id", group.getCalendarId());
        jsonObject.put("Gruppe_id", group.getSuperGroupId());
        return jsonObject;
    }

    public static JSONObject toJSON(Appointment appointment) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", appointment.getId());
        jsonObject.put("tittel", appointment.getTittel());
        jsonObject.put("description", appointment.getDescription());
        jsonObject.put("start_time", appointment.getStartTime());
        jsonObject.put("end_time", appointment.getEndTime());
        jsonObject.put("Person_id", appointment.getPersonId());
        jsonObject.put("Room_id", appointment.getRoomId());
        return jsonObject;
    }

    public static JSONArray toJSONAppointments(ArrayList<Appointment> appointments) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Appointment appointment : appointments){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", appointment.getId());
            jsonObject.put("tittel", appointment.getTittel());
            jsonObject.put("description", appointment.getDescription());
            jsonObject.put("start_time", appointment.getStartTime());
            jsonObject.put("end_time", appointment.getEndTime());
            jsonObject.put("Person_id", appointment.getPersonId());
            jsonObject.put("Room_id", appointment.getRoomId());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray toJSONPersons(ArrayList<Person> persons) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Person person : persons){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", person.getId());
            jsonObject.put("email", person.getEmail());
            jsonObject.put("firstname", person.getFirstName());
            jsonObject.put("surname", person.getSurname());
            jsonObject.put("password", person.getPassword());
            jsonObject.put("alarm_time", person.getAlarmTime());
            jsonObject.put("Calendar_id", person.getCalendarId());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public static JSONArray toJSONGroups(ArrayList<Group> groups) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Group group : groups){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", group.getId());
            jsonObject.put("name", group.getName());
            jsonObject.put("Calendar_id", group.getCalendarId());
            jsonObject.put("Gruppe_id", group.getSuperGroupId());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    */

    public static JSONObject toJSON(Appointment appointment) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

        String start_time = appointment.getStartTime().toString();
        String end_time = appointment.getEndTime().toString();
        jsonObject.put("start_time", start_time);
        jsonObject.put("end_time", end_time);
        jsonObject.put("id", appointment.getId());
        jsonObject.put("tittel", appointment.getTitle());
        jsonObject.put("Person_id", appointment.getPersonId());
        jsonObject.put("Room_id", appointment.getRoomId());
        jsonObject.put("description", appointment.getDescription());

        return jsonObject;
    }

    public static JSONObject toJSON(Group group) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", group.getName());
        jsonObject.put("id",group.getId());
        jsonObject.put("Calendar_id",group.getCalendar_id());
        return jsonObject;
    }

    public static JSONObject toJSON (Person person) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", person.getId());
        jsonObject.put("email", person.getEmail());
        jsonObject.put("firstname", person.getFirstName());
        jsonObject.put("surname", person.getSurname());
        jsonObject.put("password", person.getPassword());
        jsonObject.put("alarm_time", person.getAlarmTime());
        jsonObject.put("Calendar_id", person.getCalendarId());
        return jsonObject;
    }

    public static Group toGroup(JSONObject jsonObject) throws JSONException {
        Group group = new Group();
        group.setId(jsonObject.getInt("id"));
        group.setName(jsonObject.getString("name"));
        group.setCalendar_id(jsonObject.getInt("Calendar_id"));
        group.setSupergroup(jsonObject.getInt("Gruppe_id"));
        return group;
    }

    public static JSONArray toJSONPersons(ArrayList<Person> persons) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Person person : persons){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", person.getId());
            jsonObject.put("email", person.getEmail());
            jsonObject.put("firstname", person.getFirstName());
            jsonObject.put("surname", person.getSurname());
            jsonObject.put("password", person.getPassword());
            jsonObject.put("alarm_time", person.getAlarmTime());
            jsonObject.put("Calendar_id", person.getCalendarId());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }



    public static ArrayList<Person> toPersonArrayList(JSONArray jsonArray) throws JSONException {
        ArrayList<Person> persons = new ArrayList<>();
        for (int i = 0 ; i <jsonArray.length() ; i ++){
            persons.add(toPerson(jsonArray.getJSONObject(i)));
        }
        return persons;
    }

    public static Person toPerson(JSONObject jsonObject) throws JSONException {
        Person person = new Person();
        person.setId(jsonObject.getInt("id"));
        person.setEmail(jsonObject.getString("email"));
        person.setFirstName(jsonObject.getString("firstname"));
        person.setSurname(jsonObject.getString("surname"));
        person.setPassword(jsonObject.getString("password"));
        person.setAlarmTime(jsonObject.getInt("alarm_time"));
        person.setCalendarId(jsonObject.getInt("Calendar_id"));
        return person;
    }

    public static Appointment toAppointment(JSONObject jsonObject) throws JSONException {
        Appointment appointment = new Appointment();
        appointment.setId(jsonObject.getInt("id"));
        appointment.setTitle(jsonObject.getString("tittel"));
        appointment.setDescription(jsonObject.getString("description"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
        appointment.setStartTime(LocalDateTime.parse(jsonObject.getString("start_time"), formatter));
        appointment.setEndTime(LocalDateTime.parse(jsonObject.getString("end_time"), formatter));
        appointment.setParticipating(jsonObject.getBoolean("participating"));
        return appointment;
    }
}
