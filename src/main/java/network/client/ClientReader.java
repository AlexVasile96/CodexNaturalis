package network.client;

import network.client.Cli.HandlingPlayerInputsThreadClient;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientReader implements Runnable {
    private BufferedReader in;
    private  ClientView clientView;

    private HandlingPlayerInputsThreadClient handlingPlayerInputsThreadClient;


    //CLI CONSTRUCTOR, DOVREMO POI CAPIRE COME FARE UN GUI CONSTRUCTOR
    public ClientReader(BufferedReader in, HandlingPlayerInputsThreadClient handlingPlayerInputsThreadClient, ClientView clientView) {
        this.in = in;
        this.handlingPlayerInputsThreadClient = handlingPlayerInputsThreadClient;
    }

    @Override
    public void run() {
        String response;
        while (true) {
            try {                                                //If the server disconnects, interrupt the client
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

    private void elaborateResponse(String response) {
    }
}

