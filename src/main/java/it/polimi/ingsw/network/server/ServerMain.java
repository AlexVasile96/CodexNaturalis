package it.polimi.ingsw.network.server;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain {
    private static List<HandlingPlayerInputsThread> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    private static Game game = new Game();
    private static List<Player> playersInCurrentGame = new ArrayList<>();

    public static void main(String[] args) {
        // Try-catch block to read the IP address and the port number from a JSON file
        try {
            InputStream inputStream = ServerMain.class.getClassLoader().getResourceAsStream("HostAndPort.json");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: HostAndPort.json");
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
            for (int i = 0; i < hostAndPortArray.length(); i++) {
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
                String hostName = hostAndPort.getString("hostName");
                int portNumber = hostAndPort.getInt("portNumber");
                System.out.println("HostName: " + hostName);
                System.out.println("PortNumber: " + portNumber);
                startServer(portNumber);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // PRIVATE METHODS
    private static void startServer(int port) {
        System.out.println("Server started!");
        ServerSocket serverSocket; // Creates a ServerSocket for handling connections
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        System.out.println("Server ready for connections!");
        while (true) {
            try {
                Socket socket = serverSocket.accept(); // Aspettando il client
                String clientAddress = socket.getInetAddress().getHostAddress();
                System.out.println("Client connected from IP: " + clientAddress); // IP del client
                ServerLobby lobby = new ServerLobby(clients, socket, game);
                HandlingPlayerInputsThread clientThread = new HandlingPlayerInputsThread(socket, playersInCurrentGame, clients, lobby, game);
                clients.add(clientThread);
                pool.execute(clientThread); // Handling single player client
            } catch (IOException ex) {
                System.err.println("Client disconnection: " + ex.getMessage());
                break;
            }
        }
    }
}
