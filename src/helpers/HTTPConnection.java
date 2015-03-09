package helpers;

import models.Person;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class HTTPConnection {

    String jsonString;

    public HTTPConnection() {
        this("http://requestb.in/p3gn1mp3");
    }

    public HTTPConnection(String urlString) {
        this("http://78.91.74.53:2009/", "morten@live.com", "heisann");
    }

    public HTTPConnection(String urlString, String username, String password) {
        URL url = null;
        try {
            url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(yolo.getBytes().length));
            connection.setRequestProperty("Content-Language", "no-NO");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            jsonString = "";

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                jsonString += inputLine;
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getJSONData() {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    public ArrayList<Person> getPersons() {
        ArrayList<Person> persons = new ArrayList<>();
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(json);
//        for (Person person : json.getJSONArray()) {
//            persons.add(person);
//        }
        return persons;
    }
}
