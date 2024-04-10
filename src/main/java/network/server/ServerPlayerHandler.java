package network.server;

import Exceptions.OperationCancelledException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import controller.GameController;
import model.Card;
import network.JsonUtils;
import network.message.MessageSender;
import network.message.MessagesEnum;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

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


        try {
            System.out.println("Logging in player...");

            sendMessageToClient(MessagesEnum.INFO, "Please, set your username.");

            //Reads the client's message
            String messageString;
            while (true) {

                messageString = in.nextLine();
                System.out.println("Received message from client: " + messageString); // Print received message for debugging

                MessageSender message = gson.fromJson(messageString, MessageSender.class);

                switch (message.getMessages()) {
                    case USERNAME -> loginPlayer(message.getMessageToSend());
                    /*case NUM_OF_PLAYERS -> setGameSize(message.getMessageToSend());
                    case COMMAND -> runCommand(message.getMessageToSend());*/
                    case PING -> {
                        //does nothing, it only resets the connection timer
                    }
                    default -> {
                        System.out.println("Client sent an unexpected message: ");
                        System.out.println(message.getMessages());
                        sendMessageToClient(MessagesEnum.ERROR, "This type of message is not supported.");
                    }
                }
            }

        } catch (NoSuchElementException | IllegalStateException ex) {
            if (controller == null) {
                System.out.println("The connection with a player in login phase was lost.");
            } else if (!controller.isSizeSet()) {
                System.out.println("The connection with player " + username + " was lost during game size setting phase.");
                lobby.abortGame(controller);
            } else {
                System.out.println("The connection with player " + username + " was lost during the game.");
                controller.setDisconnectedStatus(username);
            }
        }

    }
    private void loginPlayer(String messageContent) throws NoSuchElementException {

        //If the player has not already logged in
        if (controller == null) {
            //Ask the lobby to validate username
            try {

                controller = lobby.login(messageContent, out);
                this.username = messageContent;

                if (!controller.isSizeSet())
                    sendMessageToClient(MessagesEnum.INFO, "Please, choose the game's number of players.");

            } catch (Exception ex) {
                sendMessageToClient(MessagesEnum.ERROR, ex.getMessage());
                ex.printStackTrace();
            }

        } else {
            System.out.println("Player attempted to login after already choosing a username.");
            sendMessageToClient(MessagesEnum.ERROR, "You have already chosen a username.");
        }
    }

    private void actionsInput(String userInput) {
        try {
            switch (userInput) {
                // ...

                case "getCards" -> sendCardsToClient();

                // ...
            }
        } catch (OperationCancelledException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void sendCardsToClient() {
        List<Card> cards = new ArrayList<>(); // Assume che tu abbia una lista di carte da inviare
        String json = JsonUtils.toJson(cards);
        sendMessageToClient(MessagesEnum.CARDS, json);
    }
    private void sendMessageToClient(MessagesEnum type, String content) {
        out.println(JsonUtils.toJson(new MessageSender(type, content)));
    }

    /*try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }


        //createPinger();

        try {
            System.out.println("Logging in player...");
            sendMessageToClient(MessagesEnum.INFO, "Please, set your username:");
            String messageString; //Reading client message
            while (true) {
                messageString = in.nextLine();
                MessageSender message = gson.fromJson(messageString, MessageSender.class); //GETTING THE MESSAGE FROM THE MESSAGSENDER
            }
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }*/


    /*private void sendMessage(MessagesEnum type, String message) {
        out.println(gson.toJson(new MessageSender(type, message)));
    }*/
}