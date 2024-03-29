package com.example.proj;

import java.util.ArrayList;
import java.util.List;

public class InitialCard{
    private int id;
    private Corner TL;
    private Corner TR;
    private Corner BL;
    private Corner BR;
    private List<SpecificSeed> attribute; //This list has all the specificSeed needed to have more points at the end of the game if you are able to get the specific objectives

    public InitialCard(int id, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> attribute) {
        this.id = id;
        this.TL = TL;
        this.TR = TR;
        this.BL = BL;
        this.BR = BR;
        this.attribute = attribute;
    }
}

