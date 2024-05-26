package model.game;

import com.google.gson.JsonObject;

public class Node {
    private SpecificSeed specificNodeSeed;
    private Corner corner;
    private int coordX;
    private int coordY;
    private int valueCounter;
    private  SpecificSeed firstPlacement;
    private  SpecificSeed secondPlacement;
    private boolean alreadyChecked;
    private SpecificSeed cardType;

    public Node(SpecificSeed specificNodeSeed, int coordX, int coordY, SpecificSeed cardType){
        this.specificNodeSeed=specificNodeSeed;
        this.coordX=coordX;
        this.coordY=coordY;
        this.valueCounter=2;
        this.firstPlacement= SpecificSeed.EMPTY;
        this.secondPlacement= SpecificSeed.EMPTY;
        this.alreadyChecked=false;
        this.cardType=cardType;
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

    public boolean isAlreadyChecked() {
        return alreadyChecked;
    }

    public void setAlreadyChecked(boolean alreadyChecked) {
        this.alreadyChecked = alreadyChecked;
    }
    public int getRow() {
        return coordY;
    }



    public SpecificSeed getCardType() {
        return cardType;
    }

    public void setCardType(SpecificSeed cardType) {
        this.cardType = cardType;
    }
    public static Node fromJsonObject(JsonObject jsonObject) {
        SpecificSeed specificNodeSeed = SpecificSeed.valueOf(jsonObject.get("specificNodeSeed").getAsString());
        int coordX = jsonObject.get("coordX").getAsInt();
        int coordY = jsonObject.get("coordY").getAsInt();
        int valueCounter = jsonObject.get("valueCounter").getAsInt();
        SpecificSeed firstPlacement = SpecificSeed.valueOf(jsonObject.get("firstPlacement").getAsString());
        SpecificSeed secondPlacement = SpecificSeed.valueOf(jsonObject.get("secondPlacement").getAsString());
        boolean alreadyChecked = jsonObject.get("alreadyChecked").getAsBoolean();
        SpecificSeed cardType = SpecificSeed.valueOf(jsonObject.get("cardType").getAsString());

        Node node = new Node(specificNodeSeed, coordX, coordY,cardType);
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
        jsonObject.addProperty("coordX", coordX);
        jsonObject.addProperty("coordY", coordY);
        jsonObject.addProperty("valueCounter", valueCounter);
        if (firstPlacement != null) {
            jsonObject.addProperty("firstPlacement", firstPlacement.toString());
        } else {
            jsonObject.addProperty("firstPlacement", SpecificSeed.EMPTY.toString());
        }

        if (secondPlacement != null) {
            jsonObject.addProperty("secondPlacement", secondPlacement.toString());
        } else {
            jsonObject.addProperty("secondPlacement", SpecificSeed.EMPTY.toString());
        }

        jsonObject.addProperty("alreadyChecked", alreadyChecked);

        if (cardType != null) {
            jsonObject.addProperty("cardType", cardType.toString());
        } else {
            jsonObject.addProperty("cardType", SpecificSeed.EMPTY.toString());
        }

        return jsonObject;
    }
    }

