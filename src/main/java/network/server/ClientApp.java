package network.server;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.Socket;


public class ClientApp{
    private static final String SERVER_ADDRESS = "localhost"; //127.0.0.1
    private static final int PORT = 1234;
    private int threadId;

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader("src/main/resources/HostAndPort.json"); // Leggi da file JSON
        JSONObject jsonObject = new JSONObject(new JSONTokener(reader));
        JSONArray hostAndPortArray = jsonObject.getJSONArray("hostandport");
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String hostName=null;
        int portNumber=0;

        for (int i = 0; i < hostAndPortArray.length(); i++) {
            JSONObject hostAndPort = hostAndPortArray.getJSONObject(i);
            hostName = hostAndPort.getString("hostName");
            portNumber = hostAndPort.getInt("portNumber");

            System.out.println("HostName: " + hostName);
            System.out.println("PortNumber: " + portNumber);
        }
        Socket socket = new Socket(hostName, 1234);
    }
    public static void startCLI(Socket clientSocket, BufferedReader stdIn) throws IOException {

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void run() {


        /*try (Socket socket = new Socket(hostName, 1234);
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server. You can start typing messages.");
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.write("Thread " + threadId + ": " + userInput);
                out.newLine(); // Aggiungi una nuova linea per indicare la fine del messaggio
                out.flush(); // Assicura che i dati vengano inviati

                // Attendi la risposta dal server e stampala
                String serverResponse = in.readLine();
                System.out.println("Server: " + serverResponse);
                if (serverResponse == null) {
                    System.out.println("Connessione chiusa dal server.");
                    break;
                }

                // Controlla se l'utente vuole uscire
                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Terminazione...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
