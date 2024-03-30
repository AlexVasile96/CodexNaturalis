package com.example.proj;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Node[][] nodes;
    private InitialCard initialCard;
    private ArrayList<Card> cardsOnTheBoardList;

    private int numOfEmpty; //int that counts all the empty SpecficSeed on the Board
    private SpecificSeed initEmptyValue; //this helps us initializing all the nodes to empty as start

    public Board(int rows, int cols) { //initializing all the nodes using the constructor
        cardsOnTheBoardList = new ArrayList<>();
        nodes = new Node[rows][cols];
        this.initEmptyValue =SpecificSeed.EMPTY;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodes[i][j] = new Node(SpecificSeed.EMPTY, i, j);
                this.numOfEmpty++;
            }
        }
    }

    public Node getNode(int x, int y) { //getnode method
        return nodes[x][y];
    }

    public void setNode(int x, int y, Node node) {
        nodes[x][y] = node;
    }

    public void printBoard() { //printBoardmethod
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                System.out.print(nodes[i][j].getSpecificNodeSeed() + "\t" + i + "\t" + j+ ", valore:" + nodes[i][j].getValueCounter() + " |");
            }
            System.out.println();
        }
    }


    public int[][] getCentralCoordinates() { //these coordinates are going to be the deafult-initialcards-coordinates
        int rows = nodes.length;
        int cols = nodes[0].length;
        int[][] centralCoordinates = new int[2][2];
        centralCoordinates[0][0] = rows / 2 - 1; // x
        centralCoordinates[0][1] = cols / 2 - 1; // y
        centralCoordinates[1][0] = rows / 2;     // x
        centralCoordinates[1][1] = cols / 2;     // y
        return centralCoordinates;
    }

    public void placeInitialCard(InitialCard initialCard) { //METHOD TO PLACE THE FIRST CARD WHICH IS CHOOSEN RANDOMLY
        int[][] centralCoordinates = getCentralCoordinates();
        int centerX = centralCoordinates[0][0];
        int centerY = centralCoordinates[0][1];
        //IS THE INITIAL CARD ALREADY BEEN PLACED?
        if (getNode(centerX, centerY).getValueCounter()<2 ) {
            System.out.println("Already Placed!");
            return;
        }

        //CHECKING IF I CAN PLACE THE CARD IN THE BOARD
        if (centerX >= 0 && centerX < nodes.length && centerY >= 0 && centerY < nodes[0].length) { //SETTING CORRECT NODES
            Corner TOPLEFT= initialCard.getTL();
            SpecificSeed TOPLEFTING= TOPLEFT.getSpecificCornerSeed();
            getNode(centerX, centerY).setSpecificNodeSeed(TOPLEFTING);
            getNode(centerX, centerY).setValueCounter(getNode(centerX,centerY).getValueCounter()-1); //METTE A MENO 1 Il VALUE COUNTER DELLA BOARD

            Corner TOPRIGHT= initialCard.getTR();
            SpecificSeed TOPRIGHTING= TOPRIGHT.getSpecificCornerSeed();
            getNode(centerX, centerY+1).setSpecificNodeSeed(TOPRIGHTING);
            getNode(centerX, centerY+1).setValueCounter(getNode(centerX,centerY+1).getValueCounter()-1);

            Corner BOTTOMLEFT= initialCard.getBL();
            SpecificSeed BOTTOMLEFTING= BOTTOMLEFT.getSpecificCornerSeed();
            getNode(centerX+1,centerY).setSpecificNodeSeed(BOTTOMLEFTING);
            getNode(centerX+1, centerY).setValueCounter(getNode(centerX+1,centerY).getValueCounter()-1);

            Corner BOTTOMRIGHT= initialCard.getBR();
            SpecificSeed BOTTOMRIGHITING= BOTTOMRIGHT.getSpecificCornerSeed();
            getNode(centerX+1,centerY+1).setSpecificNodeSeed(BOTTOMRIGHITING);
            getNode(centerX+1, centerY+1).setValueCounter(getNode(centerX+1,centerY+1).getValueCounter()-1);

            this.numOfEmpty=numOfEmpty-4;
            initialCard.setIndexOnTheBoard(1); //SETTING THE INDEX ON THE FIRST CARD PLACED
            cardsOnTheBoardList.add(initialCard); //ADDING THE CARD TO THE LIST THAT CONTAINS ALL THE CARD PLACED ON THE BOARD ****STORICO****
            System.out.println("Le carte che sono ora presenti sulla board sono:"); //PLN
            for(Card card : cardsOnTheBoardList)
            {
                System.out.println(card);
            }
            System.out.println("Carte Finite"); //I PRINTED ALL THE CARDS I HAVE ON MY BOARD
        } else {
            System.out.println("You can't place the initial card here");
        }
    }

    public List<Corner> getAvailableCorners() { //I WANNA SAVE ALL THE FREE CORNERS I HAVE ON MY BOARD
        List<Corner> availableCorners = new ArrayList<>();

        for (Card card : cardsOnTheBoardList) {
            availableCorners.add(card.getTL());
            availableCorners.add(card.getTR());
            availableCorners.add(card.getBL());
            availableCorners.add(card.getBR());
        }

        return availableCorners;
    }



    public void printCornerCoordinates() {
        int[][] centralCoordinates = getCentralCoordinates();
        int centerX = centralCoordinates[0][0];
        int centerY = centralCoordinates[0][1];
        // PRINTING THE COORDINATES JUST TO DEBUG
        System.out.println("Initial card coordinates:");
        System.out.println("TL: (" + centerX + ", " + centerY + ")");
        System.out.println("TR: (" + centerX + ", " + (centerY + 1) + ")");
        System.out.println("BL: (" + (centerX + 1) + ", " + centerY + ")");
        System.out.println("BR: (" + (centerX + 1) + ", " + (centerY + 1) + ")");
    }

    public void setNodes(Node[][] nodes) {
        this.nodes = nodes;
    }

    public void setInitialCard(InitialCard initialCard) {
        this.initialCard = initialCard;
    }

    public void setCardsOnTheBoardList(ArrayList<Card> cardsOnTheBoardList) {
        this.cardsOnTheBoardList = cardsOnTheBoardList;
    }

    public void setNumOfEmpty(int numOfEmpty) {
        this.numOfEmpty = numOfEmpty;
    }

    public void setInitEmptyValue(SpecificSeed initEmptyValue) {
        this.initEmptyValue = initEmptyValue;
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public InitialCard getInitialCard() {
        return initialCard;
    }

    public ArrayList<Card> getCardsOnTheBoardList() {
        return cardsOnTheBoardList;
    }

    public int getNumOfEmpty() {
        return numOfEmpty;
    }

    public SpecificSeed getInitEmptyValue() {
        return initEmptyValue;
    }
}
