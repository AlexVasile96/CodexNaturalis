package network.client.gui;

import model.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Controller {

    BufferedReader in;
    PrintWriter out;
    private int gameSize;
    private String currentPlayerNickname;

    public Controller(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }


    public void playCardClick(int indexCardToBePlacedOn, int indexCardToPlace, String cornerSelected) throws IOException {
        if(indexCardToBePlacedOn == 100 || indexCardToPlace == 100 || cornerSelected == null){ //This if prevents player to place card without choosing any card or corner
            System.out.println("You have not selected any card to place or to be placed on or the corner");
            return;
        }
        out.println("playCard"); //sends to server the message to start the playCard method
        try {

            out.println(indexCardToPlace); //sends to server the id of the card you want your card to be placed
            System.out.println(in.readLine());

            out.println(2); //non voglio girare la carta

            String messageFromServer = in.readLine();
            do{
                System.out.println(messageFromServer);
                messageFromServer= in.readLine();
            }while (!messageFromServer.equals("exit"));

            out.println(indexCardToBePlacedOn); //sends to server the index of the card you want your card to be placed on

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
            out.println("drawCardFromWellDeck");
            out.println(indexSelectedCard);
            in.readLine();
        }


    }


    public void update() throws IOException {
        currentPlayerNickname= in.readLine();
        firstWellCard();
        secondWellCard();
        thirdWellCard();
        fourthWellCard();
        checkTypeWellCards();
        playerDeck();
        checkTypePlayerDeck();
        updatingResourceAndGoldDeck();
        //System.out.println(in.readLine());

    }

    private void firstWellCard() throws IOException {

        out.println("firstWellId");
        String idCard1 = in.readLine();
        //System.out.println(idCard1);
    }
    private void secondWellCard() throws IOException {
        out.println("secondWellId");
        idCard2 = in.readLine();
        //System.out.println(idCard2);
    }
    private void thirdWellCard() throws IOException {
        out.println("thirdWellId");
        idCard3 = in.readLine();
        //System.out.println(idCard3);
    }
    private void fourthWellCard() throws IOException {
        out.println("fourthWellId");
        idCard4 = in.readLine();
        //System.out.println(idCard4);
    }
    private void updatingResourceAndGoldDeck() throws IOException {
        out.println("firstCardResourceGui");
        idTopCardResourceDeck = in.readLine();
        System.out.println("la topCardResourceDeck is: "+idTopCardResourceDeck);
        out.println("firstCardGoldGui");
        idTopCardGoldDeck = in.readLine();
        System.out.println("la topCardGoldDeck is: "+idTopCardGoldDeck);
    }

    private void checkTypeWellCards()
    {
        typeCard1 = checkType(idCard1);
        typeCard2 = checkType(idCard2);
        typeCard3 = checkType(idCard3);
        typeCard4 = checkType(idCard4);
    }
    private void playerDeck() throws IOException {
        out.println("deckId");
        idHandCard1=in.readLine();
        System.out.println(idHandCard1);
        idHandCard2=in.readLine();
        System.out.println(idHandCard2);
        idHandCard3=in.readLine();
        System.out.println(idHandCard3);
    }
    private void checkTypePlayerDeck() throws IOException {
        typeHandCard1 = checkType(idHandCard1);
        typeHandCard2 = checkType(idHandCard2);
        typeHandCard3 = checkType(idHandCard3);
        in.readLine(); //spazio
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
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
