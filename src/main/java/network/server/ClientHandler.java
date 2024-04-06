package network.server;
import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public ClientHandler(Socket socket){
        this.clientSocket=socket;
    }
    @Override
    public void run(){
        try{
            out=new PrintWriter(clientSocket.getOutputStream(), true);
            in=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while((inputLine=in.readLine())!=null){
                System.out.println("Client: "+inputLine);
                String response=processInput(inputLine);
                out.println(response);
            }
            in.close();
            out.close();
            clientSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String processInput(String input) {
        return "Server answered: "+input;
    }
}
