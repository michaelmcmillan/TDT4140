package server;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

public class ServerHTTPCommunicator {

    private String protocol   = "http";
    private String port       = "1338";
    private String ip         = "127.0.0.1";
    private String hostname   = this.protocol + "://" + this.ip + ":" + this.port;

    private String username;
    private String password;

    public void setCredentials (String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String base64EncodeString (String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

    private String getCredentials () {
        return username + ":" + password;
    }

    private HashMap<String, String> getHeaders () {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + this.base64EncodeString(this.getCredentials()));
        headers.put("Content-Type", "application/json");
        headers.put("Content-Language", "no-NO");
        headers.put("User-Agent", "Kalenderklient 1.0");
        return headers;
    }

    private URL generateURLFromString (String url) {
        URL urlObject = null;

        try {
            urlObject = new URL(this.hostname + "/" + url);
        } catch (MalformedURLException error) {
            System.out.println("OHH SHIIIT url fuckup");
        }

        return urlObject;
    }


    public JSONArray getArray (String url) {
        return this.toJSONArray(this.request(url, "GET"));
    }

    public JSONObject getObject (String url) {
        return this.toJSONObject(this.request(url, "GET"));
    }

    public void post (String url, String body) {
        this.request(url, "POST", body);
    }

    public void put (String url, String body) {
        this.request(url, "PUT", body);
    }

    public void delete (String url) {
        this.request(url, "DELETE");
    }

    private String request (String url, String method) {
        return this.request(url, method, "");
    }

    private String request (String url, String method, String body) {

        URL httpURL = this.generateURLFromString(url);
        String response = "";

        try {
            HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();

            /* Set request headers */
            connection.setRequestMethod(method);
            for (String header : this.getHeaders().keySet())
                connection.setRequestProperty(header, this.getHeaders().get(header));

            /* Set request body if present */
            if (body.isEmpty() == false) {
                connection.setDoOutput(true);
                String query = body;
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(query.getBytes("UTF-8"));
                }
            }

            /* Check response codes */
            switch (connection.getResponseCode()) {
                case 404: throw new ServerException("Fant ikke URL på serveren.");
                case 501: throw new ServerException("Feil brukernavn eller passord");
            }

            /* Retrieve the response from the request */
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseLine = "";
            while ((responseLine = in.readLine()) != null)
                response += responseLine;
            in.close();

        } catch (ProtocolException protocolError) {
            System.out.println("protocol err" + protocolError.getMessage());
        } catch (IOException ioError) {
            System.out.println("io error" + ioError.getMessage());
        }

        return response;
    }

    public JSONObject toJSONObject (String body) {
        JSONObject json = null;

        try {
            json = new JSONObject (body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONArray toJSONArray (String body) {
        JSONArray json = null;

        try {
            json = new JSONArray (body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
