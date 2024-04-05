package model;


import java.util.List;

public class GoldCard extends Card{
    private Node node;
    private boolean isCardBack;

    private List<SpecificSeed> requirementsForPlacing; //This is a list of different SpecifcSeeds needed to place the Gold card on the Board
    private SpecificSeed multiplier;
    public GoldCard(int id, SpecificSeed type, int value, SpecificSeed multiplier, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> requirements) {
        super(id, type, value, TL, TR, BL, BR);
        this.requirementsForPlacing=requirements;
        this.multiplier=multiplier;
        this.isCardBack=false;
        Corner TLBack = new Corner(SpecificSeed.EMPTY, 0, 0);
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

    public SpecificSeed getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(SpecificSeed multiplier) {
        this.multiplier = multiplier;
    }
}
