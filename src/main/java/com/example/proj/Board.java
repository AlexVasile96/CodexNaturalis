package com.example.proj;

public class Board {
    private Node[][] nodes;
    private InitialCard initialCard;
    private int numOfEmpty; //int that counts all the empty SpecficSeed on the Board
    private SpecificSeed initEmptyValue; //this helps us initializing all the nodes to empty as start

    public Board(int rows, int cols) { //initializing all the nodes using the constructor
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
        if (getNode(centerX, centerY).getCorner() != null /*||
                getNode(centerX, centerY + 1).getCorner() != null ||
                getNode(centerX + 1, centerY).getCorner() != null ||
                getNode(centerX + 1, centerY + 1).getCorner() != null*/) {
            System.out.println("Already Placed!");
        }

        //CHECKING IF I CAN PLACE THE CARD IN THE BOARD
        if (centerX >= 0 && centerX < nodes.length && centerY >= 0 && centerY < nodes[0].length) {
            //SETTING CORRECT NODES
            getNode(centerX, centerY).setCorner(initialCard.getTL());
            getNode(centerX, centerY + 1).setCorner(initialCard.getTR());
            getNode(centerX + 1, centerY).setCorner(initialCard.getBL());
            getNode(centerX + 1, centerY + 1).setCorner(initialCard.getBR());
        } else {
            System.out.println("You can't place the initial card here");
        }
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


}
