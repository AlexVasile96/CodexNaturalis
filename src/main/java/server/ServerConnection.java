package server;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerConnection implements Runnable {
    private Socket socket;


    public ServerConnection(Socket server) throws IOException {
        this.socket = server;
    }

    @Override //Handling inputs from the server, just waiting for the server to say something
    public void run() {
        try{
            System.out.println("Siamo dentro la run");
            BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out=new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdin= new BufferedReader(new InputStreamReader(System.in)); //scanner
            while (true) {
                System.out.println("<");
                String command = stdin.readLine();
                if (command.equals("quit")) break;
                out.println(command);
                String serverResponse = in.readLine();
                System.out.println("Server says: " + serverResponse);
                System.out.println("<");
                String loginName = stdin.readLine();
                out.println(loginName);
                String risposta = in.readLine();
                System.out.println("Server says: " + risposta);
                String okay = in.readLine();
                System.out.println("Server says: " + okay);
                System.out.println("sei in attesa");
                String ascolto = in.readLine();
                System.out.println("Server says: " + ascolto);
                ordinePlayer(socket, stdin, in);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void ordinePlayer(Socket clientSocket, BufferedReader stdIn, BufferedReader input) throws IOException, InterruptedException {
        Boolean uscitaCheck = Boolean.valueOf(input.readLine());
        while (uscitaCheck != false) {
            String ordinePlayer = input.readLine();
            System.out.println("Server says: " + ordinePlayer);
            uscitaCheck = Boolean.valueOf(input.readLine());
        }
    }
}

