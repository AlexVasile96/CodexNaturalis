package com.example.proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String nickName;
    private int playerScore;
    private Dot dot;
    private Board board;
    //private InitialCard initialCard;
    private ArrayList <Card> playerCards;
    public Player(String nickName, int playerScore, Dot dot, Board board){
        this.nickName = nickName;
        this.playerScore = playerScore;
        this.dot = dot;
        this.board = board;
        this.playerCards = new ArrayList<Card>(3);
    }

    public void visualizePlayerCards(List<Card> cards){
        for(Card card: cards){
            System.out.println(card);
        }
    }

    public void drawResourceCard(ResourceDeck deck) {
        deck.drawCard(this);
    }
    public void drawGoldCard(GoldDeck deck) {
        deck.drawCard(this);
    }

    //public void playCard()


    public String getNickName() {
        return nickName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public Dot getDot() {
        return dot;
    }

    public Board getBoard() {
        return board;
    }

    public Card chooseCard(int index) {
        if (index < 0 || index >= playerCards.size()) {
            System.out.println("Not valid index");
            return null;
        }
        return playerCards.get(index);
    }

    public void playCard(Board board, int cardIndex) {
        Card selectedCard = chooseCard(cardIndex);
        if (selectedCard == null) {
            return;
        }

        List<Corner> availableCorners = board.getAvailableCorners();
        System.out.println("Angoli disponibili sul tabellone:");
        for (int i = 0; i < availableCorners.size(); i++) {
            System.out.println((i + 1) + ". " + availableCorners.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose the corner you want to place the card on: ");
        int cornerIndex = scanner.nextInt();
        if (cornerIndex < 1 || cornerIndex > availableCorners.size()) {
            System.out.println("You can't place your card here");
            return;
        }

        // Ottieni l'angolo selezionato
        Corner selectedCorner = availableCorners.get(cornerIndex - 1);

        //IMPLEMENT HOW TO PLACE THE CARD METHOD->-< THIS ONE IS WRONG


        int x = selectedCorner.getX(); //THIS DOESM'T GIVE ME THE CORRECT POSITION ON THE BOARD
        int y = selectedCorner.getY();
        if (board.getNode(x, y).getValueCounter()==0) { //CHECKING IF I CAN ACTUALLY PLACEE THE CARD ON THE BOARD
            System.out.println("L'angolo selezionato non è vuoto. Scegli un altro angolo.");
            return;
        }

        // Aggiorna il tabellone con i nuovi valori della carta
        board.getNode(x, y).setSpecificNodeSeed(selectedCard.getTL().getSpecificCornerSeed());
        board.getNode(x, y).setValueCounter(board.getNode(x,y).getValueCounter()-1); //DECREASING THE VALUE

        board.getNode(x, y + 1).setSpecificNodeSeed(selectedCard.getTR().getSpecificCornerSeed());
        board.getNode(x, y+1).setValueCounter(board.getNode(x,y+1).getValueCounter()-1); //DECREASING THE VALUE

        board.getNode(x + 1, y).setSpecificNodeSeed(selectedCard.getBL().getSpecificCornerSeed());
        board.getNode(x+1, y).setValueCounter(board.getNode(x+1,y).getValueCounter()-1); //DECREASING THE VALUE

        board.getNode(x + 1, y + 1).setSpecificNodeSeed(selectedCard.getBR().getSpecificCornerSeed());
        board.getNode(x+1, y+1).setValueCounter(board.getNode(x,y).getValueCounter()-1); //DECREASING THE VALUE


        selectedCard.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); //ADDING THE CARD ON THE BOARD
        board.getCardsOnTheBoardList().add(selectedCard);

        System.out.println("Carta piazzata correttamente sull'angolo selezionato.");
    }



    public List<Card> getPlayerCards() {
        return playerCards;
    }
}
