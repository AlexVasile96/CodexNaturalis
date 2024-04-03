package com.example.proj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class ResourceCard extends Card{
    private Node node;
    private Corner TLBack;
    private Corner TRBack;
    private Corner BLBack;
    private Corner BRBack;

    public ResourceCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR) {
        super(id, type, value, TL, TR, BL, BR);
        this.TLBack = new Corner(SpecificSeed.EMPTY,0,0);
        this.TRBack = new Corner(SpecificSeed.EMPTY,0,0);
        this.BLBack = new Corner(SpecificSeed.EMPTY,0,0);
        this.BRBack = new Corner(SpecificSeed.EMPTY,0,0);
        //BACKUPORIGINAL CORNERS
        this.TLBack.setSpecificCornerSeed(TL.getSpecificCornerSeed());
        this.TRBack.setSpecificCornerSeed(TR.getSpecificCornerSeed());
        this.BLBack.setSpecificCornerSeed(BL.getSpecificCornerSeed());
        this.BRBack.setSpecificCornerSeed(BR.getSpecificCornerSeed());
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