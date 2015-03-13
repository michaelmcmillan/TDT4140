package server;

import models.Appointment;
import models.Group;
import models.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Server {

    private static Server instance = null;
    ServerHTTPCommunicator server = new ServerHTTPCommunicator();

    public void logInAs (String username, String password) {
        server.setCredentials(username, password);
    }

    public boolean isAuthenticated () {
        JSONObject json = server.getObject("user/me");
        return !json.isNull("id");
    }

    public ArrayList<Appointment> getAppointments (int calendarId, LocalDate fromDate, LocalDate toDate) {

        Date newFromDate = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date newToDate = Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fromDateFormatted = format.format(newFromDate)  ;
        String toDateFormatted = format.format(newToDate);

        JSONArray json = server.getArray("calendar/"+ calendarId +"/appointments/" + fromDateFormatted + "/" + toDateFormatted);

        ArrayList<Appointment> appointments = new ArrayList<>();
        for (int i = 0; i < json.length(); i++) {
            try {
                Appointment appointment = new Appointment();
                appointment.setTitle(json.getJSONObject(i).getString("tittel"));
                appointment.setDescription(json.getJSONObject(i).getString("description"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");

                appointment.setStartTime(LocalDateTime.parse(json.getJSONObject(i).getString("start_time"), formatter));
                appointment.setEndTime(LocalDateTime.parse(json.getJSONObject(i).getString("end_time"), formatter));

                appointments.add(appointment);

            } catch (JSONException error) {
                error.printStackTrace();
            }
        }

        return appointments;
    }


    public ArrayList<Group> getGroups () {

        JSONArray json = server.getArray("user/groups");
        ArrayList<Group> groups = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            try {
                Group group = new Group();
                group.setName(json.getJSONObject(i).getString("name"));
                groups.add(group);
            } catch (JSONException error) {
                error.printStackTrace();
            }
        }

        return groups;
    }

    public String createAppointment(int calendarId, Appointment appointment) {
        JSONObject appointmentObject = null;
        try {
            appointmentObject = JSONTranslator.toJSON(appointment);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return server.post("appointment/" + calendarId, appointmentObject.toString());
    }

    public Group createGroup(Group group) {
        JSONObject groupObjectToBePosted = null;
        JSONObject groupObjectToBeReturned = null;

        try {
            groupObjectToBePosted = JSONTranslator.toJSON(group);
            groupObjectToBeReturned = new JSONObject(server.post("group", groupObjectToBePosted.toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return JSONTranslator.toGroup(groupObjectToBeReturned);
        } catch (JSONException error) {
            error.printStackTrace();
        }

        return null;
    }

    public ArrayList<Person> addMembersToGroup (Group group, ArrayList<Person> members) {
        int groupId = group.getId();
        String returned = "";

        try {
            returned = server.post("group/" + groupId, JSONTranslator.toJSONPersons(members).toString());
            return JSONTranslator.toPersonArrayList(new JSONArray(returned));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Person getCurrentlyLoggedInPerson () {
        JSONObject json = server.getObject("user/me");

        try {
            Person person = new Person(json.getInt("id"), json.getString("email"), json.getString("firstname"), json.getString("surname"));
            person.setCalendarId(json.getInt("Calendar_id"));
            return person;
        } catch (JSONException error) {
            error.printStackTrace();
        }

        return null;
    }

    protected Server() {
        // Exists only to defeat instantiation.
    }

    public static Server getInstance() {
        if(instance == null) {
            instance = new Server();
        }
        return instance;
    }
}
