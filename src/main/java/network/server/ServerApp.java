package network.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private static final int PORT = 12345;
    public static void main(String[] args) {
        try{
            ServerSocket serverSocket=new ServerSocket(PORT); //creating server socket with port 12345
            System.out.println("Server avviato. Attendo connessioni...");
            while(true){
                Socket clientSocket=serverSocket.accept();
                System.out.println("New connection accepted.");

                ClientHandler clientHandler=new ClientHandler(clientSocket);
                clientHandler.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
