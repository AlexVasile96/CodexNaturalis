package com.example.proj;

public class ObjectiveCard{
    private int value;
    private int id;
    private SpecificSeed specificSeedType;
    private int numberOfWhenTheGameEnds;
    private ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard; //ENUM


    public ObjectiveCard(int value, int id, SpecificSeed specificSeedType, int numberOfWhenTheGameEnds, ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard) {
        this.value = value;
        this.id = id;
        this.specificSeedType = specificSeedType;
        this.numberOfWhenTheGameEnds = numberOfWhenTheGameEnds;
        this.objectiveSpecificTypeOfCard = objectiveSpecificTypeOfCard;
    }


   /* public ObjectiveCard understandWhichObjectiveCardDoYouHave()
    {
        if(this.objectiveSpecificTypeOfCard.equals("STAIRS"))
        {
            return stairsObjectiveCard();
        }
        if(this.objectiveSpecificTypeOfCard.equals("L"))
        {
            return stairsObjectiveCard();
        }
        if(this.objectiveSpecificTypeOfCard.equals("MIX"))
        {
            return stairsObjectiveCard();
        }
        if(this.objectiveSpecificTypeOfCard.equals("BIS"))
        {
            return stairsObjectiveCard();
        }
        if(this.objectiveSpecificTypeOfCard.equals("TRIS"))
        {
            return stairsObjectiveCard();
        }
    }*/

    /*public ObjectiveCard stairsObjectiveCard(){
        SpecificSeed specificSeedPointsGiver= this.getSpecificSeedType(); //prendiamo il tipo della carta ch da punti
        if(specificSeedPointsGiver.equals("MUSHROOMS") || specificSeedPointsGiver.equals("ANIMAL"))
        {

        }
        else if(specificSeedPointsGiver.equals("PLANT") || specificSeedPointsGiver.equals("INSECT"))
        {

        }
        else
        {
            return null;
        }
    }
    public ObjectiveCard lObjectiveCard(){}
    public ObjectiveCard mixObjectiveCard(){}
    public ObjectiveCard trisObjectiveCard(){}
    public ObjectiveCard bisObjectiveCard(){}*/




    public int getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public SpecificSeed getSpecificSeedType() {
        return specificSeedType;
    }

    public int getNumberOfWhenTheGameEnds() {
        return numberOfWhenTheGameEnds;
    }

    public ObjectiveSpecificTypeOfCard getObjectiveSpecificTypeOfCard() {
        return objectiveSpecificTypeOfCard;
    }

}
