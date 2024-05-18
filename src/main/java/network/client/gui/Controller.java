package network.client.gui;

import model.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Controller {

    BufferedReader in;
    PrintWriter out;
    private int gameSize;
    private String currentPlayerNickname;

    public Controller(BufferedReader in, PrintWriter out) throws IOException {
        this.in = in;
        this.out = out;
    }


    public void playCardClick(int indexCardToBePlacedOn, int indexCardToPlace, String cornerSelected) throws IOException {
        System.out.println("indexToBePlacedOn"+indexCardToBePlacedOn + " indexToPlace" + indexCardToPlace + " corner" + cornerSelected);
        if(indexCardToBePlacedOn == 100 || indexCardToPlace == 100 || cornerSelected == null){ //This if prevents player to place card without choosing any card or corner
            System.out.println("You have not selected any card to place or to be placed on or the corner");
            return;
        }
        System.out.println("il cliente ha detto playcard");
        out.println("playCard"); //sends to server the message to start the playCard method
        try {

            out.println(indexCardToPlace); //sends to server the id of the card you want your card to be placed
            System.out.println(in.readLine()); //puoi procede

            out.println(2); //non voglio girare la carta //IMPLEMENTARE IF
            System.out.println("Non voglio girare la mia carta");
            /*String messageFromServer = in.readLine();
            do{
                System.out.println(messageFromServer);
                messageFromServer= in.readLine();
            }while (!messageFromServer.equals("exit"));*/

            out.println(indexCardToBePlacedOn-1); //sends to server the index of the card you want your card to be placed on

            String avaiableCorners= in.readLine();
            do{
                System.out.println(avaiableCorners);
                avaiableCorners= in.readLine();
            } while(!avaiableCorners.equals("end"));


            out.println(cornerSelected); //sends to server the corner of the already placed cards you want your card to be placed on

            String ultimo = in.readLine();
            System.out.println(ultimo);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawCard(String wellOrDeck, String selectedDeck, Integer indexSelectedCard) throws IOException {

        out.println("drawCard");

        if (wellOrDeck.equals("deck")) {
            out.println(wellOrDeck);
            if (selectedDeck.equals("resource")) {

                out.println("drawCardFromResourceDeck");
                in.readLine();
            } else if (selectedDeck.equals("gold")) {
                out.println("firstCardGoldGui");
                String cartaPescata = in.readLine();

                out.println("drawCardFromGoldDeck");
                in.readLine();
            }
        } else if (wellOrDeck.equals("well")) {
            out.println(wellOrDeck);
            out.println("drawCardFromWell");
            out.println(indexSelectedCard);
            System.out.println("Server says: "+in.readLine());
            System.out.println("Server says: "+in.readLine());
            System.out.println("Server says: "+in.readLine());
            System.out.println("Server says: "+in.readLine());
            System.out.println("Server says: "+in.readLine());
        }


    }





    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public String showSpecificSeed() throws IOException {
        out.println("showYourSpecificSeed");
        return in.readLine();
    }

    public String showPoints() throws IOException {
        out.println("showPoints");
        return in.readLine();
    }

    public void endTurn() throws IOException {
        out.println("endTurn");
        System.out.println("Your turn ended");
        String answer = in.readLine();
    }
    public void showBoards() throws IOException {
        //è da implementare diversamente da gui, però non so come
    }
}
