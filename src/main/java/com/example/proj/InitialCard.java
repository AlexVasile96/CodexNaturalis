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

    public int getId() {
        return id;
    }

    public Corner getTL() {
        return TL;
    }

    public Corner getTR() {
        return TR;
    }

    public Corner getBL() {
        return BL;
    }

    public Corner getBR() {
        return BR;
    }

    public List<SpecificSeed> getAttribute() {
        return attribute;
    }

    @Override
    public String toString() {
        return "InitialCard{" +
                "id=" + id +
                ", TL=" + TL +
                ", TR=" + TR +
                ", BL=" + BL +
                ", BR=" + BR +
                ", attribute=" + attribute +
                '}';
    }
}

