package model;

public class ObjectiveCard extends Card{
    private int value;
    private int id;
    private SpecificSeed specificSeedType;
    private int numberOfWhenTheGameEnds;
    private ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard; //ENUM

    public ObjectiveCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, int numberOfWhenTheGameEnds, ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard) {
        super(id, type, value, TL, TR, BL, BR);
        this.value = value;
        this.id = id;
        this.numberOfWhenTheGameEnds = numberOfWhenTheGameEnds;
        this.objectiveSpecificTypeOfCard = objectiveSpecificTypeOfCard;
    }

    @Override
    public String toString() {
        return "ObjectiveCard{" +
                "value=" + value +
                ", id=" + id +
                ", specificSeedType=" + specificSeedType +
                ", numberOfWhenTheGameEnds=" + numberOfWhenTheGameEnds +
                ", objectiveSpecificTypeOfCard=" + objectiveSpecificTypeOfCard +
                '}';
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setSpecificSeedType(SpecificSeed specificSeedType) {
        this.specificSeedType = specificSeedType;
    }

    public void setNumberOfWhenTheGameEnds(int numberOfWhenTheGameEnds) {
        this.numberOfWhenTheGameEnds = numberOfWhenTheGameEnds;
    }

    public void setObjectiveSpecificTypeOfCard(ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard) {
        this.objectiveSpecificTypeOfCard = objectiveSpecificTypeOfCard;
    }

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
