package network.server;

import javafx.application.Platform;
import network.client.gui.controllers.GameSceneController;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Map<String, ClientHandler> clientHandlers = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        System.out.println("Chat server started...");
        ServerSocket serverSocket = new ServerSocket(12346); // Porta diversa dal server di gioco
        try {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientName;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Riceve il nome del client
                clientName = in.readLine();
                synchronized (clientHandlers) {
                    clientHandlers.put(clientName, this);
                }

                String message;

                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println(clientName + ": " + message);
                        if (message.startsWith("@")) {
                            // Messaggio privato
                            int spaceIndex = message.indexOf(' ');
                            if (spaceIndex != -1) {
                                String targetClientName = message.substring(1, spaceIndex);
                                String privateMessage = message.substring(spaceIndex + 1);
                                sendPrivateMessage(targetClientName, privateMessage);
                            }
                        } else {
                            // Messaggio pubblico
                            synchronized (clientHandlers) {
                                for (ClientHandler handler : clientHandlers.values()) {
                                    if (handler != this) { // Evita di inviare il messaggio al mittente
                                        handler.out.println("(public)" + message);
                                    }
                                }
                            }
                        }
                    }
                } catch (SocketException e) {
                    handleDisconnection();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientHandlers) {
                    clientHandlers.remove(clientName);
                }
            }
        }

        private void sendPrivateMessage(String targetClientName, String message) {
            ClientHandler targetHandler;
            synchronized (clientHandlers) {
                targetHandler = clientHandlers.get(targetClientName);
            }
            if (targetHandler != null) {
                targetHandler.out.println("(Private) " + clientName + ": " + message);
            } else {
                out.println("Client " + targetClientName + " not found.");
            }
        }


        private void handleDisconnection() {
            Platform.runLater(() -> {
                // Show an alert indicating the disconnection
                showAlert("Disconnection", "A player has disconnected.");
                try {
                    // Save game progress
                    savePath();
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

    }}
