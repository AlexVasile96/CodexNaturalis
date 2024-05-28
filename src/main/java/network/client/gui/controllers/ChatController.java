package network.client.gui.controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import network.client.gui.scene.ChatScene;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
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
                            // Controlla se il messaggio ricevuto dal server è diverso dall'ultimo messaggio inviato dal client
                            if (!finalMessageFromServer.equals(lastSentMessage)) {
                                chatWindow.appendMessage(finalMessageFromServer);
                            }
                        });
                    }
                } catch (SocketTimeoutException e) {
                    handleDisconnection("Connection timed out.");
                } catch (SocketException e) {
                    handleDisconnection("Connection was reset or interrupted.");
                } catch (IOException e) {
                    handleDisconnection("An I/O error occurred: " + e.getMessage());
                }
            });
            clientThread.setDaemon(true); // Permette di terminare il thread quando l'applicazione chiude
            clientThread.start();
        } catch (IOException e) {
            handleDisconnection("Failed to connect: " + e.getMessage());
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

    private void handleDisconnection(String message) {
        Platform.runLater(() -> {
            showAlert("Disconnection", message);
            try {
                // Close resources
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
