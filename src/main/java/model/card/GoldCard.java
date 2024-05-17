package model.card;


import com.google.gson.JsonObject;
import model.game.Corner;
import model.game.Node;
import model.game.SpecificSeed;
import com.google.gson.JsonObject;
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
        Corner TLBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        Corner TRBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        Corner BLBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        Corner BRBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        //BACKUPORIGINAL CORNERS
        TLBack.setSpecificCornerSeed(TL.getSpecificCornerSeed(),type);
        TRBack.setSpecificCornerSeed(TR.getSpecificCornerSeed(),type);
        BLBack.setSpecificCornerSeed(BL.getSpecificCornerSeed(),type);
        BRBack.setSpecificCornerSeed(BR.getSpecificCornerSeed(),type);
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


    //)JSON  OBJECT TO SAVE GOLD CARDS INFORMATION

    public JsonObject toJsonObject(){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("specificSeedType", type.ordinal());
        jsonObject.addProperty("value", valueWhenPlaced);
        jsonObject.addProperty("TopLeftCorner", String.valueOf(TL));
        jsonObject.addProperty("TopRightCorner", String.valueOf(TR));
        jsonObject.addProperty("BottomLeftCorner", String.valueOf(BL));
        jsonObject.addProperty("BottomRightCorner", String.valueOf(BR));
        jsonObject.addProperty("requirements", requirementsForPlacing.toString());
        jsonObject.addProperty("multiplier", multiplier.ordinal());
        return jsonObject;
    }
    public static GoldCard fromJsonObject(JsonObject jo){
        int id = jo.get("id").getAsInt();
        int specificSeedIndex = jo.get("specificSeedType").getAsInt();
        SpecificSeed specificSeed = SpecificSeed.values()[specificSeedIndex];
        int value = jo.get("value").getAsInt();
        Corner TL = Corner.fromJsonObject(jo.getAsJsonObject("TopLeftCorner"));
        Corner TR = Corner.fromJsonObject(jo.getAsJsonObject("TopRightCorner"));
        Corner BL = Corner.fromJsonObject(jo.getAsJsonObject("BottomLeftCorner"));
        Corner BR = Corner.fromJsonObject(jo.getAsJsonObject("BottomRightCorner"));
        //return new Card(id, specificSeed, value, TL, TR, BL, BR);
        return null;
    }

}
