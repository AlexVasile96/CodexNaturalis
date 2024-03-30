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
        List<Card> cardsPlayerCanChooseFrom = board.getCardsOnTheBoardList();
        System.out.println("Cards on the board are:");
        for (int i = 0; i < cardsPlayerCanChooseFrom.size(); i++) {
            Card card = cardsPlayerCanChooseFrom.get(i);
            System.out.println((i + 1) + ". " + card);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Select the card: ");
        int selectedCardIndex = scanner.nextInt();
        if (selectedCardIndex < 1 || selectedCardIndex > cardsPlayerCanChooseFrom.size()) {
            System.out.println("Indice non valido. Riprova.");
            return;
        }
        Card cardPlayerChoose = cardsPlayerCanChooseFrom.get(selectedCardIndex - 1);
        System.out.println("Carta selezionata correttamente.");

        List<Node> availableCorners = new ArrayList<>();
        availableCorners.add(cardPlayerChoose.getTL());
        availableCorners.add(cardPlayerChoose.getTR());
        availableCorners.add(cardPlayerChoose.getBL());
        availableCorners.add(cardPlayerChoose.getBR());


        System.out.println("Angoli disponibili sul tabellone:");
        for (int i = 0; i < availableCorners.size(); i++) {
            System.out.println((i + 1) + ". " + availableCorners.get(i));
        }

        System.out.print("Choose the corner you want to place the card on: ");
        int cornerIndex = scanner.nextInt();
        if (cornerIndex < 1 || cornerIndex > availableCorners.size()) {
            System.out.println("You can't place your card here");
            return;
        }
        Node selectedCorner = availableCorners.get(cornerIndex - 1);
        int x= cardPlayerChoose.getNode().getCoordX();
        int y= cardPlayerChoose.getNode().getCoordY();
        board.getNode(x, y).setSpecificNodeSeed(selectedCard.getTL().getSpecificCornerSeed());
        board.getNode(x, y).setValueCounter(board.getNode(x, y).getValueCounter() - 1); // Decrease the value

        board.getNode(x, y + 1).setSpecificNodeSeed(selectedCard.getTR().getSpecificCornerSeed());
        board.getNode(x, y + 1).setValueCounter(board.getNode(x, y + 1).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 1, y).setSpecificNodeSeed(selectedCard.getBL().getSpecificCornerSeed());
        board.getNode(x + 1, y).setValueCounter(board.getNode(x + 1, y).getValueCounter() - 1); // Decrease the value

        board.getNode(x + 1, y + 1).setSpecificNodeSeed(selectedCard.getBR().getSpecificCornerSeed());
        board.getNode(x + 1, y + 1).setValueCounter(board.getNode(x + 1, y + 1).getValueCounter() - 1); // Decrease the value

        // Add the selected card to the board
        selectedCard.setIndexOnTheBoard(board.getCardsOnTheBoardList().size() + 1); // Add the card to the board with a new index
        board.getCardsOnTheBoardList().add(selectedCard);

    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void setDot(Dot dot) {
        this.dot = dot;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }
}
