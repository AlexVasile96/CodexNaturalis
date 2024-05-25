package network.client.gui;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Configs {
    private String hostName;
    private int portNumber;

//takes address and port of the server from a json file
    public Configs() throws FileNotFoundException {
        try {
            FileReader reader = new FileReader("src/main/resources/HostAndPort.json");
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");

            for (int i = 0; i < hostAndPortArray.length(); i++) {
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
                String hostName = hostAndPort.getString("hostName");
                int portNumber = hostAndPort.getInt("portNumber");

                System.out.println("HostName: " + hostName);
                System.out.println("PortNumber: " + portNumber);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIp() {
        return hostName;
    }

    public int getPort() {
        return portNumber;
    }
}

