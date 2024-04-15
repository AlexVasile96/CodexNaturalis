package network.client.Cli;


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
    public static void main(String[] args) throws IOException {
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
        System.out.println("Client connected!");
        startCLI(socket, stdIn);
    }
    // Start the Command Line Interface
    private static void startCLI(Socket clientSocket, BufferedReader stdIn) throws IOException { //Start the Command Line Interface
        try{
        System.out.println("Creazione del client in corso...\n");
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        ClientView ClientView= new ClientView(); //Class that saves player information
        HandlingPlayerInputsThreadClient HandlingPlayerInputsThreadClient = new HandlingPlayerInputsThreadClient(in,out,ClientView); //handling player inputs
        Thread thread = new Thread(HandlingPlayerInputsThreadClient); //Start the thread
        thread.start();
        //Creates the thread that processes messages from the server
        ClientReader ClientReader = new ClientReader(in,HandlingPlayerInputsThreadClient, ClientView);
        Thread readerThread= new Thread(ClientReader);
        readerThread.start();


        //Kills the writer thread
        //HandlingPlayerInputsThreadClient.doClose();

    } catch (IOException ex) {
        System.out.println("Uh-oh, there's been an IO problem!");
    }

        //System.out.println("Shut down.");
}



}

