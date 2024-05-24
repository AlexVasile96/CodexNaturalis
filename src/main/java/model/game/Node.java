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

    public Node(SpecificSeed specificNodeSeed, int coordX, int coordY){
        this.specificNodeSeed=specificNodeSeed;
        this.coordX=coordX;
        this.coordY=coordY;
        this.valueCounter=2;
        this.firstPlacement= SpecificSeed.EMPTY;
        this.secondPlacement= SpecificSeed.EMPTY;
        this.alreadyChecked=false;
    }

    public SpecificSeed getSpecificNodeSeed() {
        return specificNodeSeed;
    }

    public String getStringSpecificNodeSeed() {
        return String.valueOf(specificNodeSeed);
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
    public static Node createNodeFromSeed(String seed, int coordX, int coordY) {
        SpecificSeed specificNodeSeed = SpecificSeed.valueOf(seed); // Converte la stringa del seme in un'istanza di SpecificSeed

        // Crea un nuovo oggetto Node utilizzando il seme e le coordinate specificate
        return new Node(specificNodeSeed, coordX, coordY);
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
        int value= jsonObject.get("value").getAsInt();
        return new Node(specificNodeSeed, coordX, coordY);
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("specificNodeSeed", specificNodeSeed.toString());
        jsonObject.addProperty("coordX", coordX);
        jsonObject.addProperty("coordY", coordY);
        jsonObject.addProperty("value", valueCounter);
        return jsonObject;
    }
}
