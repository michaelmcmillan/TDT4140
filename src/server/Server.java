package server;

import models.Appointment;
import models.Group;
import models.Person;
import models.Room;
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
                appointment.setId(json.getJSONObject(i).getInt("id"));
                appointment.setPersonId(json.getJSONObject(i).getInt("Person_id"));
                appointment.setRoomId(json.getJSONObject(i).getInt("Room_id"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
                appointment.setStartTime(LocalDateTime.parse(json.getJSONObject(i).getString("start_time"), formatter));
                appointment.setEndTime(LocalDateTime.parse(json.getJSONObject(i).getString("end_time"), formatter));
                appointment.setParticipating(json.getJSONObject(i).getBoolean("participating"));

                appointments.add(appointment);

            } catch (JSONException error) {
                error.printStackTrace();
            }
        }

        return appointments;
    }

    public ArrayList<Person> getAllUsers (){
        JSONArray json = server.getArray("user");
        try {
            return JSONTranslator.toPersonArrayList(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void joinAppointment (int appointmentId) {
        server.post("appointment/" + appointmentId + "/participants", "");
    }

    public void declineAppointment (int appointmentId) {
        server.delete("appointment/" + appointmentId + "/participants");
    }

    public ArrayList<Group> getGroups () {

        JSONArray json = server.getArray("user/groups");
        ArrayList<Group> groups = new ArrayList<>();
        try {
            groups = JSONTranslator.toGroupArrayList(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public ArrayList<Group> getSupergroups () {
        ArrayList<Group> groups = new ArrayList<Group>();
        JSONArray json = server.getArray("supergroups");

        try {
            groups = JSONTranslator.toGroupArrayList(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groups;
    }

    public ArrayList<Person> getMembersOfGroup (int groupId) {
        ArrayList<Person> persons = new ArrayList<Person>();
        JSONArray json = server.getArray("group/" + groupId + "/members");

        try {
            persons = JSONTranslator.toPersonArrayList(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return persons;
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

    public void updateAppointment(Appointment appointment) {
        JSONObject appointmentObject = null;
        try {
            appointmentObject = JSONTranslator.toJSON(appointment);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        server.put("appointment/" + appointment.getId(), appointmentObject.toString());
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

    public void deleteAppointment(Appointment appointment) {
        server.delete("appointment/" + appointment.getId());
    }

    public void updateGroup(Group group) {
        JSONObject groupObjectToBePosted = null;

        try {
            groupObjectToBePosted = JSONTranslator.toJSON(group);
            server.put("group/" + group.getId(), groupObjectToBePosted.toString());
        } catch (JSONException error) {
            error.printStackTrace();
        }
    }

    public Void addMembersToGroup (Group group, ArrayList<Person> members) {
        int groupId = group.getId();
        String returned = "";

        try {
            server.post("group/" + groupId +"/members", JSONTranslator.toJSONPersons(members).toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Person createPerson(Person person){
        JSONObject personObjectToBePosted = null;
        JSONObject personObjectToBeReturned = null;

        try {
            personObjectToBePosted = JSONTranslator.toJSON(person);
            personObjectToBeReturned = new JSONObject(server.post("user", personObjectToBePosted.toString()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }
    
    public void leaveGroup(Group group){
        server.delete("group/" + group.getId());
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

    public ArrayList<Room> getRoomSuggestions(Group group,String startTime,String endTime){
        // JSONArray jsonArray = server.getArray("room/appointment/" + appointment.getId() + "/"+startTime+"/" +endTime);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("from",startTime);
            jsonObject.put("to",endTime);

        } catch (JSONException e){
            e.printStackTrace();
        }

        String result = server.post("room/group/"+group.getId(),jsonObject.toString());


        try {
            JSONArray jsonArray = new JSONArray(result);
            return JSONTranslator.toRoomArrayList(jsonArray);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;


    };

    public ArrayList<Room> getRoomSuggestions(Appointment appointment ,String startTime,String endTime){
       // JSONArray jsonArray = server.getArray("room/appointment/" + appointment.getId() + "/"+startTime+"/" +endTime);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("from",startTime);
            jsonObject.put("to",endTime);

        } catch (JSONException e){
            e.printStackTrace();
        }

        String result = server.post("room/appointment/"+appointment.getId(),jsonObject.toString());


        try {
            JSONArray jsonArray = new JSONArray(result);
            return JSONTranslator.toRoomArrayList(jsonArray);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    };

    protected Server() {
        // Exists only to defeat instantiation.
    }

    public static Server getInstance() {
        if(instance == null) {
            instance = new Server();
        }
        return instance;
    }

    public ArrayList<Person> getAttendees (Appointment appointment) {
        ArrayList<Person> persons = new ArrayList<Person>();
        JSONArray json = server.getArray("appointment/" + appointment.getId() + "/members");

        try {
            persons = JSONTranslator.toPersonArrayList(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return persons;
    }

    public ArrayList<Room> getAllRooms (){
        ArrayList<Room> roomList = new ArrayList<>();
        JSONArray roomArray = server.getArray("/room");
        try {
            roomList.addAll(JSONTranslator.toRoomArrayList(roomArray));
        }catch (JSONException e){
            e.printStackTrace();
        }

        return roomList;
    }


}
