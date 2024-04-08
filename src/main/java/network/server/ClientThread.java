package network.server;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable{
    private static final String SERVER_ADDRESS = "localhost"; //127.0.0.1
    private static final int PORT = 12345;
    private int threadId;
    public ClientThread(int threadId){
        this.threadId=threadId;
    }

    public static void main(String[] args) {
        Thread[] client=new Thread[4];
        for(int i=0;i<client.length;i++){
            client[i] =new Thread(new ClientThread(i));
            client[i].start();
        }
    }
    @Override
    public void run() {
        /*try (Socket socket = new Socket("localhost", 1234);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println("Thread "+threadId+": "+userInput);

                // Attendi la risposta dal server e stampala
                System.out.println("Server: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try (Socket socket = new Socket("localhost", 1234);
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
        }
    }
}
