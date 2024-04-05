package com.example.proj;

public class Node {
    private SpecificSeed specificNodeSeed;
    private Corner corner;
    private int coordX;
    private int coordY;
    private int valueCounter;
    private  SpecificSeed firstPlacement;
    private  SpecificSeed secondPlacement;
    public Node(SpecificSeed specificNodeSeed, int coordX, int coordY){
        this.specificNodeSeed=specificNodeSeed;
        this.coordX=coordX;
        this.coordY=coordY;
        this.valueCounter=2;
        this.firstPlacement= SpecificSeed.EMPTY;
        this.secondPlacement= SpecificSeed.EMPTY;
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

    public void setSpecificNodeSeed(SpecificSeed specificNodeSeed) {
        this.specificNodeSeed = specificNodeSeed;
    }
    public void setCorner(Corner corner) {
        this.corner = corner;
    }

    public Corner getCorner() {
        return corner;
    }

    public void setValueCounter(int valueCounter) {
        this.valueCounter = valueCounter;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public SpecificSeed getFirstPlacement() {
        return firstPlacement;
    }

    public void setFirstPlacement(SpecificSeed firstPlacement) {
        this.firstPlacement = firstPlacement;
    }

    public SpecificSeed getSecondPlacement() {
        return secondPlacement;
    }

    public void setSecondPlacement(SpecificSeed secondPlacement) {
        this.secondPlacement = secondPlacement;
    }
}
