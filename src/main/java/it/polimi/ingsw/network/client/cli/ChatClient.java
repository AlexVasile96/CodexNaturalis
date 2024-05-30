package it.polimi.ingsw.network.client.cli;

import it.polimi.ingsw.network.server.ChatServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ChatClient {

    static String hostName;
    static int portNumber;

    public static void main(String[] args) {
        try {
            InputStream inputStream = ChatServer.class.getClassLoader().getResourceAsStream("chatServer.json");
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: chatServer.json");
            }

            JSONObject jsonObject = new JSONObject(new JSONTokener(inputStream));
            JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
            for (int i = 0; i < hostAndPortArray.length(); i++) {
                JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
                hostName = hostAndPort.getString("hostName");
                portNumber = hostAndPort.getInt("portNumber");
                System.out.println("HostName: " + hostName);
                System.out.println("PortNumber: " + portNumber);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Socket socket = new Socket(hostName, portNumber);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to chat server");

            System.out.print("Enter your name: ");
            String clientName = stdIn.readLine();
            out.println(clientName);
            System.out.println("Type @ + the player username if you want to send a private message");
            // Thread to receive messages
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

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                if (userInput.startsWith("@")) {
                    // Private message
                    out.println(userInput);
                } else {
                    // Public message
                    out.println(userInput);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Press Enter to exit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
