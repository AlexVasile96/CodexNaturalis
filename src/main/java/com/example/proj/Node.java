package com.example.proj;

public class Node {
    private SpecificSeed specificNodeSeed;
    private int coordX;
    private int coordY;
    private int valueCounter;
    public Node(SpecificSeed specificNodeSeed, int coordX, int coordY){
        this.specificNodeSeed=specificNodeSeed;
        this.coordX=coordX;
        this.coordY=coordY;
        this.valueCounter=2;
    }

    public SpecificSeed getSpecificNodeSeed() {
        return specificNodeSeed;
    }

    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public int getValueCounter() {
        return valueCounter;
    }
}
