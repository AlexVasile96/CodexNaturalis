package server;


import model.game.Player;
import network.client.ClientReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import view.ClientView;
import view.cli.Cli;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * This class represents the command-line client application.
 */

public class CliClientApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Reading JSON file to get host name and port number
        FileReader reader = new FileReader("src/main/resources/HostAndPort.json");
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in)); //scanner
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
        PrintWriter out= new PrintWriter(socket.getOutputStream(),true);
        System.out.println("Client connected!");
        ClientView clientView= new ClientView();
        ServerConnection serverConnection= new ServerConnection(socket,clientView);
        serverConnection.run();
        //startCLI(socket, stdIn, input);
    }
    // Start the Command Line Interface
    private static void startCLI(Socket clientSocket, BufferedReader stdIn, BufferedReader input) throws IOException, InterruptedException { //Start the Command Line Interface


    //new Thread(serverConnection).start();
        /*while (true) {
            System.out.println("<");
            String command = stdIn.readLine();
            if (command.equals("quit")) break;
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
            String ascolto = input.readLine();
            System.out.println("Server says: " + ascolto);
            ordinePlayer(clientSocket, stdIn, input);
        }

            /*out.println(command);*/


     //

        }
    //stampa ordine giocatori da sistemare
    public static void ordinePlayer(Socket clientSocket, BufferedReader stdIn, BufferedReader input) throws IOException, InterruptedException {
        Boolean uscitaCheck = Boolean.valueOf(input.readLine());
        while (uscitaCheck != false) {
            String ordinePlayer = input.readLine();
            System.out.println("Server says: " + ordinePlayer);
            uscitaCheck = Boolean.valueOf(input.readLine());
            }
        }

    }

