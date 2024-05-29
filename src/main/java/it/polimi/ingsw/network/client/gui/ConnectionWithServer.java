package it.polimi.ingsw.network.client.gui;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionWithServer {
    private Socket socket;

    public Socket connectToServer() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("HostAndPort.json");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: HostAndPort.json");
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");

            String hostName = null;
            int portNumber = 0;
            for (int i = 0; i < hostAndPortArray.length(); i++) {
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
                hostName = hostAndPort.getString("hostName");
                portNumber = hostAndPort.getInt("portNumber");
            }
            inputStream.close();

            this.socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            System.err.println("Connection failed\n");
        }
        return socket;
    }
}
