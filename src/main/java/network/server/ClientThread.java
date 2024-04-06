package network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        try (Socket socket = new Socket("127.0.0.1", 12345);
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
        }
    }
}
