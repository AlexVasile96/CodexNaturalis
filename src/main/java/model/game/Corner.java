package model.game;

import com.google.gson.JsonObject;

public class Corner extends Node{
    private boolean isThisCornerFree;
    private SpecificSeed specificCornerSeed;
    private int x;
    private int y;
    //private Card card;

    public Corner(SpecificSeed specificNodeSeed, int coordX, int coordY) {
        super(specificNodeSeed, coordX, coordY);
        this.isThisCornerFree=true;
        this.specificCornerSeed=specificNodeSeed;

    }

    public void setThisCornerFree(boolean thisCornerFree) {
        isThisCornerFree = thisCornerFree;
    }

    public void setSpecificCornerSeed(SpecificSeed specificCornerSeed) {
        this.specificCornerSeed = specificCornerSeed;
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
        int specificSeedIndex = jo.get("specificCornerSeed").getAsInt();
        SpecificSeed specificSeed = SpecificSeed.values()[specificSeedIndex];
        int x = jo.get("x").getAsInt();
        int y = jo.get("y").getAsInt();

        Corner corner = new Corner(specificSeed, x, y);
        //corner.setThisCornerFree(jo.get("isThisCornerFree").getAsBoolean());

        return corner;
    }

}
