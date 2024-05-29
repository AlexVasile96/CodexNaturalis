package it.polimi.ingsw.network.client.cli;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ChatClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12346;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to chat server");

            // Invia il nome del client
            System.out.print("Enter your name: ");
            String clientName = stdIn.readLine();
            out.println(clientName);

            // Thread per ricevere messaggi
            Thread receiveMessages = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("\n" + message);
                    }
                } catch (SocketException e) {
                    System.out.println("Connection closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveMessages.start();

            // Leggere e inviare messaggi
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Aggiungi una pausa prima di chiudere
        System.out.println("Press Enter to exit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
