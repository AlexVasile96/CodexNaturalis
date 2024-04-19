package model.card;

import model.Corner;
import model.Node;
import model.SpecificSeed;

import java.util.List;

public class InitialCard extends Card{
    private int indexOnTheBoard;
    private List<SpecificSeed> attributes;
    private Node node;
    private boolean isCardBack;
    private Corner TLBack;
    private Corner TRBack;
    private Corner BLBack;
    private Corner BRBack;
    private Corner TLIBack;
    private Corner TRIBack;
    private Corner BLIBack;
    private Corner BRIBack;
    public InitialCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, Corner TLIBack, Corner TRIBack, Corner BLIBack, Corner BRIBack, List<SpecificSeed> attirbutes) {
        super(id, type, value, TL, TR, BL, BR);
        this.attributes = attirbutes;
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
        this.TLIBack=TLIBack;
        this.TRIBack=TRIBack;
        this.BLIBack=BLIBack;
        this.BRIBack=BRIBack;
    }




    @Override
    public String toString() {
        return "InitialCard{" +
                " id=" + id +
                ", attributes=" + attributes +
                "\n     Front:  TL=" + TL +
                ", TR=" + TR +
                ", BL=" + BL +
                ", BR=" + BR +
                "\n     Retro:  TLBack=" + TLIBack +
                ", TRBack=" + TRIBack +
                ", BLBack=" + BLIBack +
                ", BRBack=" + BRIBack +
                '}';
    }

    @Override
    public boolean isCardBack() {
        return isCardBack;
    }

    @Override
    public void setCardBack(boolean cardBack) {
        isCardBack = cardBack;
    }

    //GETTER AND SETTER

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

    public Corner getTLIBack() {
        return TLIBack;
    }

    public void setTIBack(Corner TLIBack) {
        this.TLIBack = TLIBack;
    }

    public Corner getTRIBack() {
        return TRIBack;
    }

    public void setTRIBack(Corner TRIBack) {
        this.TRIBack = TRIBack;
    }

    public Corner getBLIBack() {
        return BLIBack;
    }

    public void setBLIBack(Corner BLIBack) {
        this.BLIBack = BLIBack;
    }

    public Corner getBRIBack() {
        return BRIBack;
    }

    public void setBRIBack(Corner BRIBack) {
        this.BRIBack = BRIBack;
    }

    @Override
    public Node getNode() {
        return node;
    }
    @Override
    public void setNode(Node node) {
        this.node = node;
    }
    public int getIndexOnTheBoard() {
        return indexOnTheBoard;
    }
    public void setIndexOnTheBoard(int indexOnTheBoard) {
        this.indexOnTheBoard = indexOnTheBoard;
    }
    public List<SpecificSeed> getAttributes() {
        return attributes;
    }
}

