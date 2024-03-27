package com.example.proj;


import java.util.ArrayList;
import java.util.List;

public class InitialCard extends Card{
    private List<SpecificSeed> attribute; //This list has all the specificSeed needed to have more points at the end of the game if you are able to get the specific objectives
    public InitialCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> attribute) {
        super(id, type, value, TL, TR, BL, BR);
        this.attribute=attribute;
    }
}

