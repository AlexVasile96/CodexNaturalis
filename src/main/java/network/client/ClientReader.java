package network.client;

import com.google.gson.Gson;
import server.HandlingPlayerInputsThread;
import network.message.MessageSender;
import network.message.MessagesEnum;
import view.ClientView;

import java.io.IOException;
import java.io.ObjectInputStream;

//This class represents a thread responsible for reading messages from the server in the client-side.

public class ClientReader implements Runnable {
    private final ObjectInputStream in;
    private final ClientView clientView;
    private final HandlingPlayerInputsThread handlingPlayerInputsThread;


    //CLI constructor, we'll figure out how to make a GUI constructor later.
    public ClientReader(ObjectInputStream in, HandlingPlayerInputsThread handlingPlayerInputsThread, ClientView clientView) {
        this.in =in;
        this.handlingPlayerInputsThread = handlingPlayerInputsThread;
        this.clientView = clientView;
    }

    @Override
    public void run() {
        while (true) {
            try {//If the server disconnects, interrupt the client
                //System.out.println("SERVER CONNESSO");
                String response = in.readLine();
                System.out.println(response);
                if (response == null) {
                    System.err.println("Server connection lost.");
                    break;
                }
                //Elaborate the message from the server
                elaborateResponse(response);
            } catch (IOException ex) {
                System.err.println("Server connection lost.");
                break;
            }
        }
    }


    public void elaborateResponse(String jsonMessage){
        // Method for processing the server response will be implemented here.
        //System.out.println("Sono in eleaborate Response Del Clientreader");
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
                    clientView.setUserName(response.getMessageToSend());
                    System.out.println("Username was correctly set to: " + clientView.getUserName() + ".");
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

