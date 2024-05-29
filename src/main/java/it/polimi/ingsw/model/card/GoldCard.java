package it.polimi.ingsw.model.card;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.Corner;
import it.polimi.ingsw.model.game.Node;
import it.polimi.ingsw.model.game.SpecificSeed;

import java.util.ArrayList;
import java.util.List;

public class GoldCard extends Card{
    private Node node;
    private boolean isCardBack;

    private List<SpecificSeed> requirementsForPlacing; //This is a list of different SpecifcSeeds needed to place the Gold card on the Board
    private SpecificSeed multiplier;
    public GoldCard(int id, SpecificSeed type, int value, SpecificSeed multiplier, Corner TL, Corner TR, Corner BL, Corner BR, List<SpecificSeed> requirements) {
        super(id, type, value, TL, TR, BL, BR);
        this.requirementsForPlacing=requirements;
        this.multiplier=multiplier;
        this.isCardBack=false;
        Corner TLBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        Corner TRBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        Corner BLBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        Corner BRBack = new Corner(SpecificSeed.EMPTY, 0, 0,type);
        //BACKUPORIGINAL CORNERS
        TLBack.setSpecificCornerSeed(TL.getSpecificCornerSeed(),type);
        TRBack.setSpecificCornerSeed(TR.getSpecificCornerSeed(),type);
        BLBack.setSpecificCornerSeed(BL.getSpecificCornerSeed(),type);
        BRBack.setSpecificCornerSeed(BR.getSpecificCornerSeed(),type);
    }

    @Override
    public String toString() { //overridden ToStringMethod
        return "Gold Card->" +
                " id=" + getId()+
                ", type=" + getType() +
                ", value=" + getValueWhenPlaced() +
                ", TL=" + getTL() +
                ", TR=" + getTR() +
                ", BL=" + getBL() +
                ", BR=" + getBR() +
                ", REQUISITI=" + requirementsForPlacing;
    }

    public String printCardRedably() {
        return "Gold Card-> id=" + id + ", value=" + valueWhenPlaced + ", type=" + type + ", REQUISITI -->" + requirementsForPlacing + ", Corners:\n     TL=" + TL + ", TR=" + TR+
                "\n     BL=" + BL + ", BR=" + BR ;
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    public List<SpecificSeed> getRequirementsForPlacing() {
        return requirementsForPlacing;
    }

    public void setRequirementsForPlacing(List<SpecificSeed> requirementsForPlacing) {
        this.requirementsForPlacing = requirementsForPlacing;
    }

    @Override
    public boolean isCardBack() {
        return isCardBack;
    }

    @Override
    public void setCardBack(boolean cardBack) {
        isCardBack = cardBack;
    }

    public SpecificSeed getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(SpecificSeed multiplier) {
        this.multiplier = multiplier;
    }


    //JSON  OBJECT TO SAVE GOLD CARDS INFORMATION
    public JsonObject toJsonObject() {
        JsonObject jsonObject = super.toJsonObject();
        jsonObject.addProperty("cardType", "GoldCard");
        JsonArray requirementsArray = new JsonArray();
        for (SpecificSeed seed : requirementsForPlacing) {
            requirementsArray.add(seed.toString());
        }
        jsonObject.add("requirementsForPlacing", requirementsArray);
        jsonObject.addProperty("multiplier", multiplier.toString());
        return jsonObject;
    }

    public static GoldCard fromJson(JsonObject jsonObject) {
        try {
            int id = jsonObject.get("id").getAsInt();
            SpecificSeed type = SpecificSeed.valueOf(jsonObject.get("type").getAsString());
            int value = jsonObject.get("value").getAsInt();

            Corner tl = Corner.fromJsonObject(jsonObject.get("TL").getAsJsonObject());
            Corner tr = Corner.fromJsonObject(jsonObject.get("TR").getAsJsonObject());
            Corner bl = Corner.fromJsonObject(jsonObject.get("BL").getAsJsonObject());
            Corner br = Corner.fromJsonObject(jsonObject.get("BR").getAsJsonObject());
            Corner tlBack = Corner.fromJsonObject(jsonObject.get("TLBack").getAsJsonObject());
            Corner trBack = Corner.fromJsonObject(jsonObject.get("TRBack").getAsJsonObject());
            Corner blBack = Corner.fromJsonObject(jsonObject.get("BLBack").getAsJsonObject());
            Corner brBack = Corner.fromJsonObject(jsonObject.get("BRBack").getAsJsonObject());

            JsonArray requirementsArray = jsonObject.get("requirementsForPlacing").getAsJsonArray();
            List<SpecificSeed> requirements = new ArrayList<>();
            for (JsonElement element : requirementsArray) {
                requirements.add(SpecificSeed.valueOf(element.getAsString()));
            }

            SpecificSeed multiplier = SpecificSeed.valueOf(jsonObject.get("multiplier").getAsString());

            GoldCard goldCard = new GoldCard(id, type, value, multiplier, tl, tr, bl, br, requirements);
            goldCard.setTLBack(tlBack);
            goldCard.setTRBack(trBack);
            goldCard.setBLBack(blBack);
            goldCard.setBRBack(brBack);

            return goldCard;
        } catch (Exception e) {
            System.err.println("Error parsing GoldCard from JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
