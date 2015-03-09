package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class HTTPConnection {

    public HTTPConnection() {
        this("http://requestb.in/p3gn1mp3");
    }

    public HTTPConnection(String urlString) {
        this("http://requestb.in/p3gn1mp3", "morten", "katt");
    }

    public HTTPConnection(String urlString, String username, String password) {
        URL url = null;
        try {
            url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String userCredentials = username + ":" + password;
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes());
            connection.setRequestProperty ("Authorization", basicAuth);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //connection.setRequestProperty("Content-Length", "" + Integer.toString(yolo.getBytes().length));
            connection.setRequestProperty("Content-Language", "no-NO");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
