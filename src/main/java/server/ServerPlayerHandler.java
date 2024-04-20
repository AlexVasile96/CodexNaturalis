package server;

import com.google.gson.Gson;
import controller.GameController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
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

    public void run(){ //METHOD TO LOGIN THE CLIENT

        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); //per scrivere
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Packet recvpacket= (Packet) in.readObject();
            System.out.println(recvpacket.message); //stampo il messaggio ricevuto dal client
            if(recvpacket.message.equals("login"))
            {
                Packet packet= new Packet("Scrivi il tuo nickname");
                out.writeObject(packet);
            }
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}


   /* private void loginPlayer(String messageContent) throws NoSuchElementException {

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
    private synchronized void sendMessageToClient(MessagesEnum type, String content) {
        try {
            if (out != null) {
                out.println(gson.toJson(new MessageSender(type, content)));
            } else {
                System.err.println("PrintWriter is null, message not sent.");
            }
        } catch (Exception e) {
            System.err.println("Error sending message to client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setGameSize(String messageContent) throws NoSuchElementException {

        //If controller number of players has not been decided
        if (!controller.isSizeSet()) {

            //Tries to set controller's number of players
            try {

                int size = Integer.parseInt(messageContent);
                controller.choosePlayerNumber(size);

            } catch (NumberFormatException ex) {
                sendMessageToClient(MessagesEnum.ERROR, "Game's number of players must be an integer.");
            } catch (Exception ex) {
                sendMessageToClient(MessagesEnum.ERROR, ex.getMessage());
            } catch (ParametersNotValidException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Player attempted to choose game's number of players without needing to.");
            sendMessageToClient(MessagesEnum.ERROR, "The game's number of players has already been decided.");
        }
    }
    private void runCommand(String messageContent) throws NoSuchElementException {

        //If player has logged in and their game's number of players has been decided
        if (controller != null && controller.isSizeSet()) {

            //Forward player command to controller
            System.out.println("Received command: " + messageContent);
            controller.readCommand(username, messageContent);
        }
    }

}*/




//                  String messageString = in.nextLine();
//                  System.out.println("Received message from client: " + messageString); // Print received message for debugging
//                  MessageSender message = gson.fromJson(messageString, MessageSender.class);
//                  switch (message.getMessages()) {
//                    case USERNAME -> loginPlayer(message.getMessageToSend());
//                    case NUM_OF_PLAYERS -> setGameSize(message.getMessageToSend());
//                    case COMMAND -> runCommand(message.getMessageToSend());
//                    case PING -> {
//                    }
//                    default -> {
//                        System.out.println("Client sent an unexpected message: ");
//                        System.out.println(message.getMessages());
//                        sendMessageToClient(MessagesEnum.ERROR, "This type of message is not supported.");
//                    }
//                }