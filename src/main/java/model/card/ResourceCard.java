package model.card;

import model.Corner;
import model.Node;
import model.SpecificSeed;

public class ResourceCard extends Card{
    private Node node;
    public ResourceCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR) {
        super(id, type, value, TL, TR, BL, BR);
        Corner TLBack = new Corner(SpecificSeed.EMPTY, 0, 0); //intellij suggetion, first it was declared as attribute
        Corner TRBack = new Corner(SpecificSeed.EMPTY, 0, 0);
        Corner BLBack = new Corner(SpecificSeed.EMPTY, 0, 0);
        Corner BRBack = new Corner(SpecificSeed.EMPTY, 0, 0);
        //BACKUPORIGINAL CORNERS
        TLBack.setSpecificCornerSeed(TL.getSpecificCornerSeed());
        TRBack.setSpecificCornerSeed(TR.getSpecificCornerSeed());
        BLBack.setSpecificCornerSeed(BL.getSpecificCornerSeed());
        BRBack.setSpecificCornerSeed(BR.getSpecificCornerSeed());
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