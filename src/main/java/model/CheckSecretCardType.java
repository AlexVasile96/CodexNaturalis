package model;

public class CheckSecretCardType extends ObjectiveCard{
    public CheckSecretCardType(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, int numberOfWhenTheGameEnds, ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard) {
        super(id, type, value, TL, TR, BL, BR, numberOfWhenTheGameEnds, objectiveSpecificTypeOfCard);
    }

}
