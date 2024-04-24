package server;


import model.game.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static List<HandlingPlayerInputsThread> clients= new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    private static List< Player> playersInCurrentGame= new ArrayList<>();
        public static void main(String[] args) {
            //Try-catch block to read the IP address and the port number from a JSON file
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
                    startServer(portNumber);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //PRIVATE METHODS
        private static void startServer(int port) {
            System.out.println("Server started!");


            // Creates a ServerSocket for handling connections
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return;
            }
            System.out.println("Server ready for connections!");
            //Creates the lobby for this server

            //Accepts connections from clients on new threads
            while (true) {
                try {

                    Socket socket = serverSocket.accept();                                          //aspettando il client
                    String clientAddress = socket.getInetAddress().getHostAddress();
                    System.out.println("Client connected from IP: " + clientAddress);//ok            // ip del client
                    ServerLobby lobby = new ServerLobby(clients, socket);
                    HandlingPlayerInputsThread clientThread= new HandlingPlayerInputsThread(socket, playersInCurrentGame, clients, lobby);
                    clients.add(clientThread);
                    pool.execute(clientThread); //Handling single player client

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    break;
                }
            }

        }

    public static int getIntClients() {
        return clients.size();
    }

    public static List<HandlingPlayerInputsThread> getClients() {
        return clients;
    }

    public static List<Player> getPlayersInCurrentGame() {
        return playersInCurrentGame;
    }
}

