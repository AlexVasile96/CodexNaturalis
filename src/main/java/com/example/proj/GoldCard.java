package com.example.proj;

import java.util.ArrayList;
import java.util.List;

public class GoldCard extends Card{
    List<SpecificSeed> requirementsForPlacing = new ArrayList<SpecificSeed>();
    public GoldCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> requirements) {
        super(id, type, value, TL, TR, BL, BR);
        this.requirementsForPlacing=requirements;
    }

    @Override
    public String toString() {
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
