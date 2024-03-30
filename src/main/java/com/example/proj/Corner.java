package com.example.proj;

public class Corner {
    private boolean isThisCornerFree;
    private SpecificSeed specificCornerSeed;
    private int x;
    private int y;

    public void setThisCornerFree(boolean thisCornerFree) {
        isThisCornerFree = thisCornerFree;
    }

    public void setSpecificCornerSeed(SpecificSeed specificCornerSeed) {
        this.specificCornerSeed = specificCornerSeed;
    }

    public Corner(SpecificSeed s) //public Corner Constructor
    {
        this.isThisCornerFree=true;
        this.specificCornerSeed=s;


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
}
