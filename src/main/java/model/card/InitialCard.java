package model.card;

import com.google.gson.JsonObject;
import model.game.Corner;
import model.game.Node;
import model.game.SpecificSeed;

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
        this.TLBack = new Corner(SpecificSeed.EMPTY,0,0,type);
        this.TRBack = new Corner(SpecificSeed.EMPTY,0,0,type);
        this.BLBack = new Corner(SpecificSeed.EMPTY,0,0,type);
        this.BRBack = new Corner(SpecificSeed.EMPTY,0,0,type);
        //BACKUPORIGINAL CORNERS
        this.TLBack.setSpecificCornerSeed(TL.getSpecificCornerSeed(),type);
        this.TRBack.setSpecificCornerSeed(TR.getSpecificCornerSeed(),type);
        this.BLBack.setSpecificCornerSeed(BL.getSpecificCornerSeed(),type);
        this.BRBack.setSpecificCornerSeed(BR.getSpecificCornerSeed(),type);
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


    public void setAttributes(List<SpecificSeed> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Corner getTLBack() {
        return TLBack;
    }

    @Override
    public void setTLBack(Corner TLBack) {
        this.TLBack = TLBack;
    }

    @Override
    public Corner getTRBack() {
        return TRBack;
    }

    @Override
    public void setTRBack(Corner TRBack) {
        this.TRBack = TRBack;
    }

    @Override
    public Corner getBLBack() {
        return BLBack;
    }

    @Override
    public void setBLBack(Corner BLBack) {
        this.BLBack = BLBack;
    }

    @Override
    public Corner getBRBack() {
        return BRBack;
    }

    @Override
    public void setBRBack(Corner BRBack) {
        this.BRBack = BRBack;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("type", type.toString());
        jsonObject.add("TL", TL.toJsonObject());
        jsonObject.add("TR", TR.toJsonObject());
        jsonObject.add("BL", BL.toJsonObject());
        jsonObject.add("BR", BR.toJsonObject());
        jsonObject.add("TLIBack", TLIBack.toJsonObject());
        jsonObject.add("TRIBack", TRIBack.toJsonObject());
        jsonObject.add("BLIBack", BLIBack.toJsonObject());
        jsonObject.add("BRIBack", BRIBack.toJsonObject());
        jsonObject.addProperty("isCardBack", isCardBack);
        jsonObject.addProperty("indexOnTheBoard", indexOnTheBoard);
        if (node != null) {
            jsonObject.add("node", node.toJsonObject());
        }
        return jsonObject;
    }

    public static InitialCard fromJsonObject(JsonObject jsonObject) {
        int id = jsonObject.get("id").getAsInt();
        SpecificSeed type = SpecificSeed.valueOf(jsonObject.get("type").getAsString());
        Corner TL = Corner.fromJsonObject(jsonObject.getAsJsonObject("TL"));
        Corner TR = Corner.fromJsonObject(jsonObject.getAsJsonObject("TR"));
        Corner BL = Corner.fromJsonObject(jsonObject.getAsJsonObject("BL"));
        Corner BR = Corner.fromJsonObject(jsonObject.getAsJsonObject("BR"));
        Corner TLIBack = Corner.fromJsonObject(jsonObject.getAsJsonObject("TLIBack"));
        Corner TRIBack = Corner.fromJsonObject(jsonObject.getAsJsonObject("TRIBack"));
        Corner BLIBack = Corner.fromJsonObject(jsonObject.getAsJsonObject("BLIBack"));
        Corner BRIBack = Corner.fromJsonObject(jsonObject.getAsJsonObject("BRIBack"));
        boolean isCardBack = jsonObject.get("isCardBack").getAsBoolean();
        InitialCard initialCard = new InitialCard(id, type, 0, TL, TR, BL, BR, TLIBack, TRIBack, BLIBack, BRIBack, null);
        initialCard.setCardBack(isCardBack);
        if (jsonObject.has("node")) {
            initialCard.setNode(Node.fromJsonObject(jsonObject.getAsJsonObject("node")));
        }
        if (jsonObject.has("indexOnTheBoard")) {
            initialCard.setIndexOnTheBoard(jsonObject.get("indexOnTheBoard").getAsInt());
        }
        return initialCard;
    }
}

