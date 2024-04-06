package network.server;


import java.io.*;
import java.net.Socket;


public class ClientApp{
    private static final String SERVER_ADDRESS = "localhost"; //127.0.0.1
    private static final int PORT = 12345;
    Thread[] client=new Thread[3];
    /*public static void main(String[] args) {
        for(int i=0;i<4;i++){
            client[i] =new Thread(new ClientThread());
            client[i].start();
        }
    }*/
}
