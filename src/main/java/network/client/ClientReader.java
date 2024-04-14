package network.client;

import network.client.Cli.HandlingPlayerInputsThreadClient;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;

//This class represents a thread responsible for reading messages from the server in the client-side.

public class ClientReader implements Runnable {
    private BufferedReader in;
    private  ClientView clientView;

    private HandlingPlayerInputsThreadClient handlingPlayerInputsThreadClient;


    //CLI constructor, we'll figure out how to make a GUI constructor later.
    public ClientReader(BufferedReader in, HandlingPlayerInputsThreadClient handlingPlayerInputsThreadClient, ClientView clientView) {
        this.in = in;
        this.handlingPlayerInputsThreadClient = handlingPlayerInputsThreadClient;
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
    /**
     * Elaborates on the response received from the server.
     *
     * @param response The response received from the server.
     */

    private void elaborateResponse(String response){
        // Method for processing the server response will be implemented here.
    }
}

