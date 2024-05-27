package network.client.gui.controllers;

import javafx.application.Platform;
import network.client.gui.scene.ChatScene;

public class ChatController {
    private ChatScene chatWindow;

    public ChatController(ChatScene chatWindow) {
        this.chatWindow = chatWindow;
    }

    public void start() {
        Thread clientThread = new Thread(() -> {
            try {
                // Simula la connessione al server
//                while (true) {
                    // Simula ricezione messaggi
                    String messageFromServer = receiveMessageFromServer();
                    Platform.runLater(() -> chatWindow.appendMessage("Server: " + messageFromServer));
                    Thread.sleep(1000); // Simula il tempo di attesa per i messaggi
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        clientThread.setDaemon(true); // Permette di terminare il thread quando l'applicazione chiude
        clientThread.start();
    }

    private String receiveMessageFromServer() {
        // Simula la ricezione di un messaggio dal server
        return "Hello from server";
    }
}
