package codex;
import server.CliClientApp;
import server.ServerMain;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class represents the main executable for the game, it allows to select between booting the server, the CLI client or the GUI client
 */

public class Codex {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hi! Welcome to Codex!!!\nWhat do you want to launch?");
        System.out.println(
                """
                        0. SERVER
                        1. CLIENT (CLI)
                        2. CLIENT (GUI)
                        
                        """);
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }
        switch (input) {
            case 0 -> ServerMain.main(null);
            case 1 -> {
                System.out.println("You selected the CLI interface, have fun!\nStarting...");
                CliClientApp.main(null); }
            case 2 -> {
                System.out.println("You selected the GUI interface, have fun!\nStarting...");
                //GUI.main(null);
            }
            default -> System.err.println("Invalid argument, please run the executable again with one of these options:\n1.server\n2.client");
        }
    }
}