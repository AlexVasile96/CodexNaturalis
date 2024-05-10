package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PongClient implements Runnable {
    private static final int PING_INTERVAL = 10000; // Intervallo di ping in millisecondi (10 secondi)
    private final PrintWriter out;
    private final BufferedReader in;

    public PongClient(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Attende un messaggio di ping dal server
                String message = in.readLine();

                // Se riceve un messaggio di ping
                if (message != null && message.equals("PING")) {
                    // Risponde con un messaggio di pong
                    out.println("PONG");
                }

                // Aspetta il prossimo messaggio di ping
                Thread.sleep(PING_INTERVAL);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
