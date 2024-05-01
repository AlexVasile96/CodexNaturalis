package network.client.gui;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;

public class ConnectionWithServer {
    private Socket socket;
    public Socket connectToServer() {
        try {

            FileReader reader = new FileReader("src/main/resources/HostAndPort.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");

            String hostName = null;
            int portNumber = 0;
            for (int i = 0; i < hostAndPortArray.length(); i++) {
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
                hostName = hostAndPort.getString("hostName");
                portNumber = hostAndPort.getInt("portNumber");
            }
            this.socket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            System.err.println("Connection failed\n");
        }
        return socket;
    }
}