package network.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import controller.GameController;
import network.message.MessageSender;
import network.message.MessagesEnum;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.Timer;

/**
 * This class handles the server's connection with a single client
 */
public class ServerPlayerHandler implements Runnable {
    private final Socket socket; //Socket for the connection with a single client
    private final ServerLobby lobby; //server lobby
    private Scanner in; //input reader
    private PrintWriter out; //outputwriter
    private GameController controller; // The controller for the game this handler's client has joined
    private String username;
    private final Gson gson; //GSON TO SERIELIZE CLIENT MESSAGES
    private Timer pingTimer; //TIMER TO VERIFY CLIENT STABILITY

    public ServerPlayerHandler(Socket socket, ServerLobby lobby) {
        this.socket = socket;
        this.lobby = lobby;
        this.controller = null;
        this.gson = new Gson();
        this.username = null;
    }

    //MULTITHREADING METHODS

    public void run() { //METHOD TO LOGIN THE CLIENT

        //Sets a 10 second timeout for the socket reader
        try {
            socket.setSoTimeout(10 * 1000);
        } catch (SocketException e) {
            System.err.println("Warning: couldn't set socket timeout in ServerPlayerHandler");
            e.printStackTrace();
        }

        //CREATING INPUT AND OUTPUT
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }


        //createPinger();

        try {
            System.out.println("Logging in player...");
            sendMessage(MessagesEnum.INFO, "Please, set your username:");
            String messageString; //Reading client message
            while (true) {
                messageString = in.nextLine();
                MessageSender message = gson.fromJson(messageString, MessageSender.class); //GETTING THE MESSAGE FROM THE MESSAGSENDER
            }
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(MessagesEnum type, String message) {
        out.println(
                gson.toJson(
                        new MessageSender(type, message)));
    }
}