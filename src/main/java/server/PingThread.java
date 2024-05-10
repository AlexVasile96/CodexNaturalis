package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PingThread implements Runnable {
    private static final int PING_INTERVAL = 10000; // Intervallo di ping in millisecondi (10 secondi)
    private final PrintWriter out;
    private final BufferedReader in;

    public PingThread(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Invia il messaggio di ping al client
                out.println("PING");

                // Aspetta la risposta del client per un certo periodo di tempo
                String response = in.readLine();

                // Controlla se la risposta è un "PONG"
                if (response != null && response.equals("PONG")) {
                    // Risposta ricevuta, attendi il prossimo intervallo di ping
                    Thread.sleep(PING_INTERVAL);
                } else {
                    // Il client non ha risposto correttamente, gestisci di conseguenza
                    // (es. segnala la disconnessione del client, chiudi la connessione, ecc.)
                    break; // Esci dal ciclo
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}