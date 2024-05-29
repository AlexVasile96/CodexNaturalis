package it.polimi.ingsw.model.game;

import com.google.gson.JsonObject;

import java.util.Objects;

public class Node {
    private SpecificSeed specificNodeSeed;
    private Corner corner;
    private int cordX;
    private int cordY;
    private int valueCounter;
    private  SpecificSeed firstPlacement;
    private  SpecificSeed secondPlacement;
    private boolean alreadyChecked;
    private SpecificSeed cardType;

    public Node(SpecificSeed specificNodeSeed, int cordX, int cordY, SpecificSeed cardType){
        this.specificNodeSeed=specificNodeSeed;
        this.cordX = cordX;
        this.cordY = cordY;
        this.valueCounter=2;
        this.firstPlacement= SpecificSeed.EMPTY;
        this.secondPlacement= SpecificSeed.EMPTY;
        this.alreadyChecked=false;
        this.cardType=cardType;
    }

    public SpecificSeed getSpecificNodeSeed() {
        return specificNodeSeed;
    }

    public int getCordX() {
        return cordX;
    }

    public int getCordY() {
        return cordY;
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

    public boolean isAlreadyChecked() {
        return alreadyChecked;
    }

    public void setAlreadyChecked(boolean alreadyChecked) {
        this.alreadyChecked = alreadyChecked;
    }



    public SpecificSeed getCardType() {
        return cardType;
    }

    public void setCardType(SpecificSeed cardType) {
        this.cardType = cardType;
    }
    public static Node fromJsonObject(JsonObject jsonObject) {
        SpecificSeed specificNodeSeed = SpecificSeed.valueOf(jsonObject.get("specificNodeSeed").getAsString());
        int cordX = jsonObject.get("cordX").getAsInt();
        int cordY = jsonObject.get("cordY").getAsInt();
        int valueCounter = jsonObject.get("valueCounter").getAsInt();
        SpecificSeed firstPlacement = SpecificSeed.valueOf(jsonObject.get("firstPlacement").getAsString());
        SpecificSeed secondPlacement = SpecificSeed.valueOf(jsonObject.get("secondPlacement").getAsString());
        boolean alreadyChecked = jsonObject.get("alreadyChecked").getAsBoolean();
        SpecificSeed cardType = SpecificSeed.valueOf(jsonObject.get("cardType").getAsString());

        Node node = new Node(specificNodeSeed, cordX, cordY,cardType);
        node.setValueCounter(valueCounter);
        node.setFirstPlacement(firstPlacement);
        node.setSecondPlacement(secondPlacement);
        node.setAlreadyChecked(alreadyChecked);
        node.setCardType(cardType);
        return node;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("specificNodeSeed", specificNodeSeed.toString());
        jsonObject.addProperty("cordX", cordX);
        jsonObject.addProperty("cordY", cordY);
        jsonObject.addProperty("valueCounter", valueCounter);
        jsonObject.addProperty("firstPlacement", Objects.requireNonNullElse(firstPlacement, SpecificSeed.EMPTY).toString());

        jsonObject.addProperty("secondPlacement", Objects.requireNonNullElse(secondPlacement, SpecificSeed.EMPTY).toString());
        jsonObject.addProperty("alreadyChecked", alreadyChecked);
        jsonObject.addProperty("cardType", Objects.requireNonNullElse(cardType, SpecificSeed.EMPTY).toString());

        return jsonObject;
    }
}

