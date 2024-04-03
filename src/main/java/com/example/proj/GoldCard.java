package com.example.proj;


import java.util.List;

public class GoldCard extends Card{
    private Node node;
    private boolean isCardBack;
    private Corner TLBack;
    private Corner TRBack;
    private Corner BLBack;
    private Corner BRBack;

    private List<SpecificSeed> requirementsForPlacing; //This is a list of different SpecifcSeeds needed to place the Gold card on the Board
    public GoldCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> requirements) {
        super(id, type, value, TL, TR, BL, BR);
        this.requirementsForPlacing=requirements;
        this.isCardBack=false;
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
    public String toString() { //overridden ToStringMethod
        return "GoldCard->" +
                " id=" + getId()+
                ", type=" + getType() +
                ", value=" + getValueWhenPlaced() +
                ", TL=" + getTL() +
                ", TR=" + getTR() +
                ", BL=" + getBL() +
                ", BR=" + getBR() +
                ", REQUISITI=" + requirementsForPlacing;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    public List<SpecificSeed> getRequirementsForPlacing() {
        return requirementsForPlacing;
    }

    public void setRequirementsForPlacing(List<SpecificSeed> requirementsForPlacing) {
        this.requirementsForPlacing = requirementsForPlacing;
    }

    @Override
    public boolean isCardBack() {
        return isCardBack;
    }

    @Override
    public void setCardBack(boolean cardBack) {
        isCardBack = cardBack;
    }
}
