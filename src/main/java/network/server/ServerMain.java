package network.server;


import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents the executable for the game on the server machine
 */

public class ServerMain {
        public static void main(String[] args) {

            try {
                FileReader reader = new FileReader("src/main/resources/HostAndPort.json"); // Leggi da file JSON
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

        /**
         * Reads connections from the given port and creates new threads to handle them
         *
         * @param port the port from which to take connections
         */
        private static void startServer(int port) {
            ExecutorService executor = Executors.newCachedThreadPool();

            System.out.println("Server started!");

            //Creates connection socket
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

            //Creates connections with clients on new threads
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Creating new connection...");
                    executor.submit(new ServerPlayerHandler(socket, lobby));
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    break;
                }
            }

            executor.shutdown();
        }
    }

