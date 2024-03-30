package com.example.proj;

import java.util.List;

public class InitialCard extends Card{
    private int indexOnTheBoard;
    private List<SpecificSeed> attribute;
    private Node node;
    public InitialCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> attirbutes) {
        super(id, type, value, TL, TR, BL, BR);
        this.attribute = attirbutes;
    }


    public int getId() {
        return id;
    }

    public Corner getTL() {
        return TL;
    }

    public Corner getTR() {
        return TR;
    }

    public Corner getBL() {
        return BL;
    }

    public Corner getBR() {
        return BR;
    }

    public int getIndexOnTheBoard() {
        return indexOnTheBoard;
    }

    public void setIndexOnTheBoard(int indexOnTheBoard) {
        this.indexOnTheBoard = indexOnTheBoard;
    }

    public List<SpecificSeed> getAttribute() {
        return attribute;
    }

    @Override
    public String toString() {
        return "InitialCard{" +
                "id=" + id +
                ", TL=" + TL +
                ", TR=" + TR +
                ", BL=" + BL +
                ", BR=" + BR +
                ", attributes=" + attribute +
                '}';
    }

    public void setTL(Corner TL) {
        this.TL = TL;
    }

    public void setTR(Corner TR) {
        this.TR = TR;
    }

    public void setBL(Corner BL) {
        this.BL = BL;
    }

    public void setBR(Corner BR) {
        this.BR = BR;
    }

    public void setAttribute(List<SpecificSeed> attribute) {
        this.attribute = attribute;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setNode(Node node) {
        this.node = node;
    }
}

