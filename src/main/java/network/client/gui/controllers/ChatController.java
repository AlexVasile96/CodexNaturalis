package network.client.gui.controllers;

import javafx.application.Platform;
import network.client.gui.scene.ChatScene;

import java.io.*;
import java.net.Socket;

public class ChatController {
    private ChatScene chatWindow;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private String lastSentMessage; // Variabile per tenere traccia dell'ultimo messaggio inviato
    private String lastReceivedMessage;
    public ChatController(ChatScene chatWindow, String clientName) {
        this.chatWindow = chatWindow;
        this.clientName = clientName;
    }

    public void start() {
        try {
            socket = new Socket("localhost", 12346); // Connettersi al server
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Invia il nome del client al server
            out.println(clientName);

            Thread clientThread = new Thread(() -> {
                try {
                    String messageFromServer;
                    while ((messageFromServer = in.readLine()) != null) {
                        String finalMessageFromServer = messageFromServer;
                        Platform.runLater(() -> {
                            if (!finalMessageFromServer.equals(lastSentMessage) && !finalMessageFromServer.equals(lastReceivedMessage)) {
                                lastReceivedMessage = finalMessageFromServer;
                                chatWindow.appendMessage(finalMessageFromServer);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientThread.setDaemon(true); // Permette di terminare il thread quando l'applicazione chiude
            clientThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(String message) {
        if (out != null) {
            String formattedMessage = clientName + "-> " + message;
            lastSentMessage = formattedMessage; // Memorizza l'ultimo messaggio inviato
            out.println(formattedMessage);

        }
    }
}
