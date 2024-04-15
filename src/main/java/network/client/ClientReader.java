package network.client;

import com.google.gson.Gson;
import network.client.Cli.HandlingPlayerInputsThreadClient;
import network.message.MessageSender;
import network.message.MessagesEnum;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;

//This class represents a thread responsible for reading messages from the server in the client-side.

public class ClientReader implements Runnable {
    private final BufferedReader in;
    private final ClientView clientView;

    private final HandlingPlayerInputsThreadClient handlingPlayerInputsThreadClient;


    //CLI constructor, we'll figure out how to make a GUI constructor later.
    public ClientReader(BufferedReader in, HandlingPlayerInputsThreadClient handlingPlayerInputsThreadClient, ClientView clientView) {
        this.in = in;
        this.handlingPlayerInputsThreadClient = handlingPlayerInputsThreadClient;
        this.clientView = clientView;
    }

    @Override
    public void run() {
        String response;
        while (true) {
            try {//If the server disconnects, interrupt the client
                response = in.readLine();
            } catch (IOException ex) {
                System.err.println("Server connection lost.");
                break;
            }
            if (response == null) {
                System.err.println("Server connection lost.");
                break;
            }
            //Elaborate the message from the server
            elaborateResponse(response);
        }
    }

    private void elaborateResponse(String jsonMessage){
        // Method for processing the server response will be implemented here.
        Gson gson = new Gson();
        MessageSender response = gson.fromJson(jsonMessage, MessageSender.class);

        try {

            switch (response.getMessages()) {

                //Information

                case INFO -> {
                    System.out.println(response.getMessageToSend());
                }

                case ERROR -> {
                    System.err.println(response.getMessageToSend());
                }

                case CONFIRM_USERNAME -> {
                    clientView.setUsername(response.getMessageToSend());
                    System.out.println("Username was correctly set to: " + clientView.getUsername() + ".");
                }

                case WAIT_PLAYERS -> {
                    clientView.setWaitPlayers(true);
                    System.out.println(response.getMessageToSend());
                }

                case GAME_START -> {
                    clientView.setWaitPlayers(false);
                    notifyViewUpdate(response.getMessages());
                    System.out.println(response.getMessageToSend());
                }

                case GAME_END -> {
                    notifyViewUpdate(response.getMessages());
                    System.out.println(response.getMessageToSend());
                }

                case PLAYER_CONNECTED -> {
                    notifyViewUpdate(response.getMessages());
                    System.out.println("Player " + response.getMessageToSend() + " has joined the game.\n");
                }

                case PLAYER_DISCONNECTED -> {
                    notifyViewUpdate(response.getMessages());
                    System.out.println("Player " + response.getMessageToSend() + " has left the game.\n");
                }
                //fagioli
            }

        } catch (Exception ex) {
            System.err.println("Warning: View update failed.");
            ex.printStackTrace();
        }

    }
    private void notifyViewUpdate(MessagesEnum response) {
            //If in CLI mode, clear the console and re-print all of the game's elements
            System.out.println(clientView);
    }
}

