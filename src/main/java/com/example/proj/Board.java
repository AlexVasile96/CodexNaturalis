package com.example.proj;

public class Board {
    private Node[][] nodes;
    private int numOfEmpty;
    private SpecificSeed initEmptyValue;

    public Board(int rows, int cols) {
        nodes = new Node[rows][cols];
        this.initEmptyValue =SpecificSeed.EMPTY;

        // Inizializzazione della board con nodi vuoti
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodes[i][j] = new Node(SpecificSeed.EMPTY, i, j);
                this.numOfEmpty++;
            }
        }
    }

    public Node getNode(int x, int y) {
        return nodes[x][y];
    }

    public void setNode(int x, int y, Node node) {
        nodes[x][y] = node;
    }

    public void printBoard() {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                System.out.print(nodes[i][j].getSpecificNodeSeed() + " " + i + " " + j+ ", valore:" + nodes[i][j].getValueCounter() + " |");
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
    public int[][] getCentralCoordinates() {
        int rows = nodes.length;
        int cols = nodes[0].length;

        int[][] centralCoordinates = new int[2][2];
        centralCoordinates[0][0] = rows / 2 - 1; // x della prima coordinata centrale
        centralCoordinates[0][1] = cols / 2 - 1; // y della prima coordinata centrale
        centralCoordinates[1][0] = rows / 2;     // x della seconda coordinata centrale
        centralCoordinates[1][1] = cols / 2;     // y della seconda coordinata centrale

        return centralCoordinates;
    }
}
