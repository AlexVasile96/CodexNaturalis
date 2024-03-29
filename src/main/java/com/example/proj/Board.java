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

    public static void main(String[] args) {
        Board board = new Board(50, 50);
        board.printBoard();
        System.out.println(board.initEmptyValue);
        System.out.println(board.numOfEmpty);
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
}
