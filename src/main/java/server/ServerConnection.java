package server;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerConnection implements Runnable{
    private Socket server;
    private BufferedReader in;
    private PrintWriter out;


    public ServerConnection(Socket socket) throws IOException {
        this.server=socket;
        in= new BufferedReader(new InputStreamReader(server.getInputStream()));
        out= new PrintWriter(server.getOutputStream(), true);
    }

    @Override //Handling inputs from the server, just waiting for the server to say something
    public void run() {
        try{
        while(true) {
            String serverResponse = in.readLine();
            //if(serverResponse==null)break;
            System.out.println("Server says: " + serverResponse);
        }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

