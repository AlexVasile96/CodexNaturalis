package codex;

import network.client.gui.GUI;
import network.client.cli.CliClientApp;
import network.server.ChatServer;
import network.server.ServerMain;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Codex {

    public static void main(String[] args) throws Exception {
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
            case 0 -> {
                System.out.println("Starting both Server and ChatServer...");
                new Thread(() -> {
                    try {
                        ServerMain.main(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                new Thread(() -> {
                    try {
                        ChatServer.main(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            case 1 -> {
                System.out.println("You selected the CLI interface, have fun!\nStarting...");
                CliClientApp.main(null); }
            case 2 -> {
                System.out.println("You selected the GUI interface, have fun!\nStarting...");
                GUI.main(null);
            }
            default -> System.err.println("Invalid argument, please run the executable again with one of these options:\n1.server\n2.client");
        }
    }
}