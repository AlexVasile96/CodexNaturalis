package com.example.proj;

public class Corner {
    private boolean isThisCornerFree;
    private SpecificSeed specificCornerSeed;

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
}
