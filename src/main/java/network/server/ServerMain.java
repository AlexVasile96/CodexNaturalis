package network.server;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
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
            ExecutorService executor = Executors.newCachedThreadPool();
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
            ServerLobby lobby = new ServerLobby();
            //Accepts connections from clients on new threads
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    String clientAddress = socket.getInetAddress().getHostAddress();
                    System.out.println("Client connected from IP: " + clientAddress);
                    executor.submit(new ServerPlayerHandler(socket, lobby)); //HAndling singol player client
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    break;
                }
            }

            executor.shutdown();
        }
    }

