package network.client.gui.controllers;

import javafx.application.Platform;
import network.client.gui.scene.ChatScene;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ChatController {
    private ChatScene chatWindow;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private String lastSentMessage; // Variabile per tenere traccia dell'ultimo messaggio inviato

    public ChatController(ChatScene chatWindow, String clientName) {
        this.chatWindow = chatWindow;
        this.clientName = clientName;
    }

    public void start() {
        try {
            socket = new Socket("192.168.1.2", 12346); // Connettersi al server
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
                            // Controlla se il messaggio ricevuto dal server è diverso dall'ultimo messaggio inviato dal client
                            if (!finalMessageFromServer.equals(lastSentMessage)) {
                                chatWindow.appendMessage(finalMessageFromServer);
                            }
                        });
                    }
                }catch (SocketTimeoutException e) {
                        handleDisconnection();
                    }
                 catch (IOException e) {
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
            String formattedMessage = clientName + ": " + message;
            lastSentMessage = formattedMessage; // Memorizza l'ultimo messaggio inviato
            out.println(formattedMessage);
        }
    }

    public void sendPrivateMessage(String targetClientName, String message) {
        if (out != null) {
            String formattedMessage = "@" + targetClientName + " " + message;
            lastSentMessage = formattedMessage; // Memorizza l'ultimo messaggio inviato
            out.println(formattedMessage);
        }
    }
    private void handleDisconnection() {
        Platform.runLater(() -> {
            // Show an alert indicating the disconnection
            try {
                // Save game progress
                // Close resources
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                // Exit the application
                Platform.exit();
                System.exit(0);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // Throw a runtime exception if an IOException occurs
                throw new RuntimeException(e);
            }
        });
    }
}
