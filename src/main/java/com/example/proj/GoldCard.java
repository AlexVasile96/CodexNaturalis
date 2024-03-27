package com.example.proj;


import java.util.List;

public class GoldCard extends Card{
    private List<SpecificSeed> requirementsForPlacing; //This is a list of different SpecifcSeeds needed to place the Gold card on the Board
    public GoldCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> requirements) {
        super(id, type, value, TL, TR, BL, BR);
        this.requirementsForPlacing=requirements;
    }

    @Override
    public String toString() { //overridden ToStringMethod
        return "GoldCard{" +
                "id=" + getId()+
                ", type=" + getType() +
                ", value=" + getValueWhenPlaced() +
                ", TL=" + getTL() +
                ", TR=" + getTR() +
                ", BL=" + getBL() +
                ", BR=" + getBR() +
                ", REQUISITI=" + requirementsForPlacing +
                '}';
    }
}
