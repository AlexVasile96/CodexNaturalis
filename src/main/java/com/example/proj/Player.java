package com.example.proj;

import java.util.ArrayList;
import java.util.List;

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

    public List<Card> getPlayerCards() {
        return playerCards;
    }
}
