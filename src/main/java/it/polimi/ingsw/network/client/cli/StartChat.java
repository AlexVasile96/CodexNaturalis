package it.polimi.ingsw.network.client.cli;

import java.io.IOException;

public class StartChat {
    public static void main(String[] args) {
        try {
            Runtime.getRuntime().exec("cmd /c start java ChatClient");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Codice del gioco qui
    }
}
