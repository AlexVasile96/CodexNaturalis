package it.polimi.ingsw.network.server;

import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    static int portNumber;
    private static Map<String, ClientHandler> clientHandlers = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        try {
            InputStream inputStream = ChatServer.class.getClassLoader().getResourceAsStream("chatServer.json");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: chatServer.json");
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
            for (int i = 0; i < hostAndPortArray.length(); i++) {
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
                String hostName = hostAndPort.getString("hostName");
                portNumber = hostAndPort.getInt("portNumber");
                System.out.println("HostName: " + hostName);
                System.out.println("PortNumber: " + portNumber);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
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

                // Receiving client name
                clientName = in.readLine();
                synchronized (clientHandlers) {
                    clientHandlers.put(clientName, this);
                }

                String message;

                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println(clientName + ": " + message);
                        if (message.startsWith("@")) {
                            // Private Message
                            int spaceIndex = message.indexOf(' ');
                            if (spaceIndex != -1) {
                                String targetClientName = message.substring(1, spaceIndex);
                                String privateMessage = message.substring(spaceIndex + 1);
                                sendPrivateMessage(targetClientName, privateMessage);
                            }
                        } else {
                            // Public Message
                            synchronized (clientHandlers) {
                                for (ClientHandler handler : clientHandlers.values()) {
                                    if (handler != this) { //ChatServer doesn't send the same message to the sender
                                        handler.out.println(clientName + ": " + message);
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
        }

    }
}