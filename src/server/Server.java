package server;

import models.Appointment;
import models.Group;
import models.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Server {

    private static Server instance = null;
    ServerHTTPCommunicator server = new ServerHTTPCommunicator();

    public void logInAs (String username, String password) {
        server.setCredentials(username, password);
    }

    public boolean isAuthenticated () {
        JSONObject json = server.getObject("user/me");

        try {
            return (json.getBoolean("success"));
        } catch (JSONException error) {
            return false;
        }
    }

    public Person getPerson (int userId) {

        JSONObject json = server.getObject("user/" + userId);
        Person person = null;

        try {
            person = new Person(json.getString("email"));
            person.setFirstName(json.getString("firstname"));
            person.setSurname(json.getString("surname"));
            person.setAlarmTime(json.getInt("alarm_time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return person;
    }

    public ArrayList<Appointment> getAppointmentsByPerson (int userId) {

        JSONArray json = server.getArray("user/" + userId + "/appointments");
        ArrayList<Appointment> appointments = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            try {
                Appointment appointment = new Appointment();
                appointment.setTitle(json.getJSONObject(i).getString("tittel"));
                appointment.setDescription(json.getJSONObject(i).getString("description"));
                appointments.add(appointment);
            } catch (JSONException error) {

            }
        }

        return appointments;
    }

    public ArrayList<Group> getGroupsByPerson (int userId) {

        JSONArray json = server.getArray("user/" + userId + "/groups");
        ArrayList<Group> groups = new ArrayList<>();

        for (int i = 0; i < json.length(); i++) {
            try {
                Group group = new Group(json.getJSONObject(i).getString("name"));
                groups.add(group);
            } catch (JSONException error) {

            }
        }

        return groups;
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