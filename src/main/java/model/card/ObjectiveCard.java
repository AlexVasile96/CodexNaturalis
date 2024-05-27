package model.card;

import com.google.gson.JsonObject;
import model.game.Corner;
import model.game.ObjectiveSpecificTypeOfCard;
import model.game.SpecificSeed;

public class ObjectiveCard extends Card{
    private int value;
    private SpecificSeed type;
    private int id;
    private SpecificSeed specificSeedType;
    private int numberOfWhenTheGameEnds;
    private ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard; //ENUM

    public ObjectiveCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR, int numberOfWhenTheGameEnds, ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard) {
        super(id, type, value, TL, TR, BL, BR);
        this.value = value;
        this.specificSeedType = type;
        this.id = id;
        this.specificSeedType = type;
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

    public static ObjectiveCard fromJsonObject(JsonObject jsonObject) {
        int id = jsonObject.get("id").getAsInt();
        SpecificSeed type = SpecificSeed.valueOf(jsonObject.get("type").getAsString());
        int value = jsonObject.get("value").getAsInt();
        Corner tl = Corner.fromJsonObject(jsonObject.get("TL").getAsJsonObject());
        Corner tr = Corner.fromJsonObject(jsonObject.get("TR").getAsJsonObject());
        Corner bl = Corner.fromJsonObject(jsonObject.get("BL").getAsJsonObject());
        Corner br = Corner.fromJsonObject(jsonObject.get("BR").getAsJsonObject());
        int numberOfWhenTheGameEnds = jsonObject.get("numberOfWhenTheGameEnds").getAsInt();
        ObjectiveSpecificTypeOfCard objectiveSpecificTypeOfCard = ObjectiveSpecificTypeOfCard.valueOf(jsonObject.get("objectiveSpecificTypeOfCard").getAsString());

        ObjectiveCard objectiveCard = new ObjectiveCard(id, type, value, tl, tr, bl, br, numberOfWhenTheGameEnds, objectiveSpecificTypeOfCard);

        // add backup of corner back if it exists
        if (jsonObject.has("TLBack")) {
            objectiveCard.setTLBack(Corner.fromJsonObject(jsonObject.get("TLBack").getAsJsonObject()));
        }
        if (jsonObject.has("TRBack")) {
            objectiveCard.setTRBack(Corner.fromJsonObject(jsonObject.get("TRBack").getAsJsonObject()));
        }
        if (jsonObject.has("BLBack")) {
            objectiveCard.setBLBack(Corner.fromJsonObject(jsonObject.get("BLBack").getAsJsonObject()));
        }
        if (jsonObject.has("BRBack")) {
            objectiveCard.setBRBack(Corner.fromJsonObject(jsonObject.get("BRBack").getAsJsonObject()));
        }

        return objectiveCard;
    }

    // METODO toJsonObject
    @Override
    public JsonObject toJsonObject() {
        JsonObject jsonObject = super.toJsonObject(); // calls the superclass' method
        jsonObject.addProperty("cardType", "ObjectiveCard");
        jsonObject.addProperty("numberOfWhenTheGameEnds", numberOfWhenTheGameEnds);
        jsonObject.addProperty("objectiveSpecificTypeOfCard", objectiveSpecificTypeOfCard.toString());
        return jsonObject;
    }
}


