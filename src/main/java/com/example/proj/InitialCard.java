package com.example.proj;


import java.util.ArrayList;
import java.util.List;

public class InitialCard extends Card{
    List<SpecificSeed> attribute = new ArrayList<SpecificSeed>();
    public InitialCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> attribute) {
        super(id, type, value, TL, TR, BL, BR);
        this.attribute=attribute;
    }
}

