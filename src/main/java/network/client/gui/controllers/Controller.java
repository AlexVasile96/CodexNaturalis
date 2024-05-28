package network.client.gui.controllers;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import network.client.gui.scene.EndGameScene;
import network.client.gui.scene.QuitScene;
import view.ClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Controller {

    BufferedReader in;
    PrintWriter out;
    private Socket socket;
    private ClientView clientView;
    public Controller(BufferedReader in, PrintWriter out, Socket socket, ClientView clientView) throws IOException {
        this.in = in;
        this.out = out;
        this.socket=socket;
        this.socket.setSoTimeout(60000);
        this.clientView=clientView;
    }


    public void playCardClick(int indexCardToBePlacedOn, int indexCardToPlace, String cornerSelected, String isTheCardFlipped) throws IOException {
        if (indexCardToBePlacedOn == 100 || indexCardToPlace == 100 || cornerSelected == null) { //This if prevents player to place card without choosing any card or corner
            System.out.println("You have not selected any card to place or to be placed on or the corner");
        } else {
            System.out.println("Client decided to place a card");
            out.println("playCard"); //sends to server the message to start the playCard method
            try {
                out.println(indexCardToPlace); //Sending to server the index of the card we want to place
                String canIContinueOrGoldCArdNotPLaceable = in.readLine(); //Server says we can continue
                if (canIContinueOrGoldCArdNotPLaceable.equals("Gold Card not placeable")) {
                    String messageFromServer = in.readLine();
                    System.out.println("gold card requires: " + messageFromServer);
                    messageFromServer = in.readLine();
                    System.out.println("you got: " + messageFromServer);
                    System.out.println("You can:\n1-> choose another card\n2-> turn the card");
                    out.println(2);
                    System.out.println(in.readLine());
                } else {
                    if (isTheCardFlipped == null || isTheCardFlipped.equals("Front")) {
                        out.println(2); //My card is not flipped
                    } else {
                        out.println(1); //My card is flipped
                    }
                }
                out.println(indexCardToBePlacedOn - 1); //Sending to the server the index of the card on the board
                String[] angoli = {"TL", "TR", "BR", "BL"};
                String[] validInputs = new String[4];
                boolean check = false;
                int size = 0;
                String messageFromServer = in.readLine();
                do {
                    for (String corner : angoli) {
                        if (messageFromServer.equals(corner) && !check) {
                            validInputs[size] = messageFromServer;
                            size++;
                            check = true;
                        }
                    }
                    if (!check) System.out.println(messageFromServer);
                    check = false;
                    messageFromServer = in.readLine();
                } while (!messageFromServer.equals("end"));
                System.out.print("Choose the corner you want to place the card on: ");
                switch (cornerSelected) {
                    case "TL":
                        out.println("TL");
                        break;
                    case "TR":
                        out.println("TR");
                        break;
                    case "BL":
                        out.println("BL");
                        break;
                    case "BR":
                        out.println("BR");
                        break;
                    default:
                        out.println("Invalid corner selected");
                        break;
                }

                System.out.println(in.readLine()); //card correctly placed
                String typeCard = in.readLine(); //For server purposes
                String isBack = in.readLine(); //For server purposes
                String coordinate = in.readLine();//For server purposes
                System.out.println(typeCard);//For server purposes
                System.out.println(isBack);//For server purposes
                System.out.println(coordinate);//For server purposes
                out.println("typeCard");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void drawCard(String wellOrDeck, String selectedDeck, Integer indexSelectedCard) throws IOException {
        try{
        out.println("drawCard");
        System.out.println("You chose to draw a card!");
        if (wellOrDeck.equals("deck")) {
            out.println(wellOrDeck);
            if (selectedDeck.equals("resource")) {

                out.println("drawCardFromResourceDeck");
                in.readLine();
            } else if (selectedDeck.equals("gold")) {
                out.println("firstCardGoldGui");
                in.readLine(); //Drawn card

                out.println("drawCardFromGoldDeck");
                in.readLine(); //Drawn card
            }
        } else if (wellOrDeck.equals("well")) {
            out.println("well");
            out.println("showWell");
            System.out.println("Which card from the well do you want to draw?");
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println("Select '0' for"+in.readLine()); //cards in the well
            System.out.println("Select '1' for"+in.readLine());
            System.out.println("Select '2' for"+in.readLine());
            System.out.println("Select '3' for"+in.readLine());
            in.readLine();//space for network purpose
            System.out.println("------------------------------------------------------------------------------------------");
            out.println(indexSelectedCard);

            //handling server responses
            String result = in.readLine();
            if(result.equals("operation performed correctly")) {
                System.out.println("Operation 'Draw card from Well' performed correctly");
                out.println("showYourCardDeck");
                System.out.println("Your Deck:" );
                System.out.println("--------------------------------------------------------------------------------------");
                receivingAndPrintingCards();
                System.out.println("--------------------------------------------------------------------------------------");
                showWell();
            }
            else{
                System.out.println("Operation failed");
            }
         }

        }catch (SocketTimeoutException e) {
            handleDisconnection();
        }
    }

    public int getPoints() throws IOException {
        out.println("status");
        try{
        return Integer.parseInt(in.readLine());}
        catch (SocketTimeoutException e)
        {
            handleDisconnection();
        }
        return 0;
    }

    public String firstCommon() throws IOException {
        out.println("firstCommon");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String secondCommon() throws IOException {
        out.println("secondCommon");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String secretCard() throws IOException {
        out.println("secret");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showWell() {
        out.println("showWell");
        try {
            System.out.println(in.readLine()); // first well card
            System.out.println(in.readLine()); // second well card
            System.out.println(in.readLine()); // third well card
            System.out.println(in.readLine()); // fourth well card
            in.readLine(); // space

        } catch (SocketTimeoutException e) {
            handleDisconnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void quit(Stage primaryStage) throws IOException {
        out.println("quit");
        try {
            System.out.println(in.readLine()); // ALL_CLIENTS_QUIT
            QuitScene quitScene = new QuitScene();
            quitScene.quit(primaryStage);
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (SocketTimeoutException e) {
            handleDisconnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String showSpecificSeed() throws IOException {
        out.println("showYourSpecificSeed");
        try {
            return in.readLine();
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String finalEnd() {
        out.println("endTurn");
        try {
            System.out.println("Your turn ended");
            String nextPlayerNickname = in.readLine();
            System.out.println(nextPlayerNickname);
            Platform.runLater(()-> showAlert("Your turn ended!", "Your turn ended! Now it's "+ nextPlayerNickname + " turn!"));
            out.flush();
            return nextPlayerNickname;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


        public String endTurn() throws IOException {
        out.println("endTurn");
        try {
            String nextPlayerNickname = in.readLine();
            System.out.println(nextPlayerNickname);
            Platform.runLater(()-> showAlert("Your turn ended!", "Your turn ended! Now it's "+ nextPlayerNickname + " turn!"));
            String update = in.readLine();
            System.out.println(update);
            out.flush();
            return nextPlayerNickname;
        } catch (SocketTimeoutException e) {
            handleDisconnection();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receivingAndPrintingCards() throws IOException {
        in.readLine(); //Reading all three cards
        in.readLine();
        in.readLine();
        in.readLine();//the space
    }

    public void waitForTurn(String playerNickname, Stage primaryStage) throws IOException {
        String message;
        System.out.println("Waiting for server to say my name");
        while (!(message = in.readLine()).equals(playerNickname)) {
            System.out.println("Received message while waiting for turn: " + message);

            switch (message) {
                case "You smashed 20 points!! now everybody got one last turn":
                    String whoGot20Points = in.readLine();
                    Platform.runLater(() -> showAlert("Someone Got 20 Points!", "WOW!!! " + whoGot20Points + " HAS FINALLY REACHED 20 POINTS!!"));
                    break;

                case "ALL_CLIENTS_QUIT":
                    quit(primaryStage);
                    break;

                case "END OF GAME!":
                    EndGameScene endGameScene = new EndGameScene(primaryStage, out, socket, in, clientView,this );
                    Platform.runLater(() -> {
                        try {
                            endGameScene.endGame();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;

                default:
                    System.out.println("Not your turn, please wait");
                    break;
            }
        }
        System.out.println("It's now " + playerNickname + "'s turn");
        Platform.runLater(()-> showAlert("It's your turn!", "Time to play some codex!"));
    }


    private void handleDisconnection() {
        System.out.println("Client disconnected or crashed.");
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Shows an alert with the given title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to be displayed in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
