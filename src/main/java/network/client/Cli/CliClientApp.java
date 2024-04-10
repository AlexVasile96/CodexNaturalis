package network.client.Cli;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import view.ClientView;

import java.io.*;
import java.net.Socket;


public class CliClientApp {
    private static final String SERVER_ADDRESS = "localhost"; //127.0.0.1
    private static final int PORT = 1234;
    private int threadId;

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader("src/main/resources/HostAndPort.json"); // READING JSON FILE
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String hostName=null;           //INITIALIZING
        int portNumber=0;               //INITIALIZING

        for (int i = 0; i < hostAndPortArray.length(); i++) {
            JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
            hostName = hostAndPort.getString("hostName");
            portNumber = hostAndPort.getInt("portNumber");
            System.out.println("HostName: " + hostName);
            System.out.println("PortNumber: " + portNumber);
        }
        Socket socket = new Socket(hostName,portNumber); //CREATING THE SOCKET
        startCLI(socket, stdIn);
    }
    private static void startCLI(Socket clientSocket, BufferedReader stdIn) throws IOException { //Start the Command Line Interface
        System.out.println("Creazione del client in corso...\n");
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ClientView clientView= new ClientView();                //Class that saves player informations
        HandlingPlayerInputsThreadClient handlingPlayerInputsThreadClient = new HandlingPlayerInputsThreadClient(in,out,clientView); //handling player inputs
        Thread thread = new Thread(handlingPlayerInputsThreadClient); //parte il thread
        thread.start();
    }


}

