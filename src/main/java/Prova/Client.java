package Prova;

import java.io.*;
import java.net.*;
class Client1
{
    public static void main(String args[])
    {
        Socket s = null;
        DataInputStream is = null;
        try
        {
            s = new Socket("localhost", 11111);
            is = new DataInputStream(s.getInputStream());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        System.out.println("Socket creata: " + s);
        try
        {
            String line;
            while( (line=is.readLine())!=null )
            {
                System.out.println("Ricevuto: " + line);
                if (line.equals("Stop"))
                    break;
            }
            is.close(); // chiusura stream
            s.close(); // chiusura socket
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}