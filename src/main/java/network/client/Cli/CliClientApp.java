package network.client.Cli;


import network.client.ClientReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import view.ClientView;

import java.io.*;
import java.net.Socket;

public class CliClientApp {
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
        System.out.println("Client connected!");
        startCLI(socket, stdIn);
    }
    private static void startCLI(Socket clientSocket, BufferedReader stdIn) throws IOException { //Start the Command Line Interface
        try{
        System.out.println("Creazione del client in corso...\n");
        PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ClientView ClientView= new ClientView();                //Class that saves player informations
        HandlingPlayerInputsThreadClient HandlingPlayerInputsThreadClient = new HandlingPlayerInputsThreadClient(in,out,ClientView); //handling player inputs
        Thread thread = new Thread(HandlingPlayerInputsThreadClient); //parte il thread
        thread.start();
        //Creates the thread that processes messages from the server
        ClientReader ClientReader = new ClientReader(in,HandlingPlayerInputsThreadClient, ClientView);
        Thread readerThread= new Thread(ClientReader);
        readerThread.start();


        //Kills the writer thread
        HandlingPlayerInputsThreadClient.doClose();

    } catch (IOException ex) {
        System.out.println("Uh-oh, there's been an IO problem!");
    }

        System.out.println("Shut down.");
}



}

