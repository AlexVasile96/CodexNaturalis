package model.game;

import com.google.gson.JsonObject;

public class Corner extends Node{
    private boolean isThisCornerFree;
    private SpecificSeed specificCornerSeed;
    private int x;
    private int y;
    private SpecificSeed cardSeed;

    public Corner(SpecificSeed specificNodeSeed, int coordX, int coordY, SpecificSeed type) {
        super(specificNodeSeed, coordX, coordY, null);
        this.isThisCornerFree=true;
        this.specificCornerSeed=specificNodeSeed;
       this.cardSeed=type;
    }

    public void setThisCornerFree(boolean thisCornerFree) {
        isThisCornerFree = thisCornerFree;
    }

    public void setSpecificCornerSeed(SpecificSeed specificCornerSeed, SpecificSeed cardType) {
        this.specificCornerSeed = specificCornerSeed;
        this.cardSeed=cardType;
    }
    public boolean isThisCornerFree() {
        return isThisCornerFree;
    }

    public SpecificSeed getSpecificCornerSeed() {
        return specificCornerSeed;
    }
    public String toString() {
        return " " +
                //"isThisCornerFree=" + isThisCornerFree +
                specificCornerSeed
                ;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Corner fromJsonObject(JsonObject jo) {
        SpecificSeed specificSeed = SpecificSeed.valueOf(jo.get("specificCornerSeed").getAsString());
        int x = jo.get("x").getAsInt();
        int y = jo.get("y").getAsInt();
        SpecificSeed type = SpecificSeed.valueOf(jo.get("cardSeed").getAsString());
        Corner corner = new Corner(specificSeed, x, y, type);
        corner.setThisCornerFree(jo.get("isThisCornerFree").getAsBoolean());
        return corner;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("specificCornerSeed", specificCornerSeed.toString());
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("cardSeed", cardSeed.toString());
        jsonObject.addProperty("isThisCornerFree", isThisCornerFree);
        return jsonObject;
    }


    public SpecificSeed getCardSeed() {
        return cardSeed;
    }

    public void setCardSeed(SpecificSeed cardSeed) {
        this.cardSeed = cardSeed;
    }
}
