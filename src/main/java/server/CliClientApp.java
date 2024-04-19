package server;


import network.client.ClientReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import view.ClientView;

import java.io.*;
import java.net.Socket;

/**
 * This class represents the command-line client application.
 */

public class CliClientApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Reading JSON file to get host name and port number
        FileReader reader = new FileReader("src/main/resources/HostAndPort.json");
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String hostName=null;         //INITIALIZING
        int portNumber=0;       //INITIALIZING

        // Iterating through the JSONArray to extract host names and port numbers.
        for (int i = 0; i < hostAndPortArray.length(); i++) {
            // Get the JSONObject representing a host and port combination.
            JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
            // Extract the host name from the JSONObject.
           hostName = hostAndPort.getString("hostName");
            // Extract the port number from the JSONObject.
           portNumber = hostAndPort.getInt("portNumber");

            System.out.println("HostName: " + hostName);
            System.out.println("PortNumber: " + portNumber);
        }
        // Creating the socket and connecting to the server
        Socket socket = new Socket(hostName,portNumber); //CREATING THE SOCKET
        BufferedReader input= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Client connected!");
        startCLI(socket, stdIn, input);
    }
    // Start the Command Line Interface
    private static void startCLI(Socket clientSocket, BufferedReader stdIn, BufferedReader input) throws IOException, InterruptedException { //Start the Command Line Interface
    PrintWriter out= new PrintWriter(clientSocket.getOutputStream(),true);
    while (true) {
     System.out.println("<");
     String command = stdIn.readLine();
     out.println(command);
     String serverResponse = input.readLine();
     System.out.println("Server says: " + serverResponse);
     System.out.println("<");
     String loginName = stdIn.readLine();
     out.println(loginName);
     String risposta = input.readLine();
     System.out.println("Server says: " + risposta);
     String okay = input.readLine();
     System.out.println("Server says: " + okay);
     System.out.println("sei in attesa");
 }
}




}

