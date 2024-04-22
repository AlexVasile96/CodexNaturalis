package server;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import view.ClientView;
import java.io.*;
import java.net.Socket;


public class CliClientApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        FileReader reader = new FileReader("src/main/resources/HostAndPort.json"); // Reading JSON file to get host name and port number
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
        String hostName=null;            //INITIALIZING
        int portNumber=0;               //INITIALIZING
        for (int i = 0; i < hostAndPortArray.length(); i++) {                    // Iterating through the JSONArray to extract host names and port numbers.
            JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);          // Get the JSONObject representing a host and port combination.
            hostName = hostAndPort.getString("hostName");                    // Extract the host name from the JSONObject.
            portNumber = hostAndPort.getInt("portNumber");                   // Extract the port number from the JSONObject.
            System.out.println("HostName: " + hostName);
            System.out.println("PortNumber: " + portNumber);
        }
        Socket socket = new Socket(hostName,portNumber);                                 // Creating the socket and connecting to the server
        System.out.println("Client connected!");                                        //Client successfully connected
        ClientView clientView= new ClientView();                                        //Giving the client a personal view
        ServerConnection serverConnection= new ServerConnection(socket,clientView); //Creating a new thread that will handle clients interactions
        serverConnection.run();                                                         //Starting each client thread
    }
}

