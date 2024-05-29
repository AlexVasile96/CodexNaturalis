package it.polimi.ingsw.model.card;


import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.Corner;
import it.polimi.ingsw.model.game.Node;
import it.polimi.ingsw.model.game.SpecificSeed;

public class ResourceCard extends Card{
    private Node node;
    public ResourceCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR) {
        super(id, type, value, TL, TR, BL, BR);
        Corner TLBack = new Corner(SpecificSeed.EMPTY, 0, 0,type); //intellij suggestion, first it was declared as attribute
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
    public Node getNode() {
        return node;
    }
    @Override
    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "Resource Card->" +
                " id=" + id +
                ", type=" + type +
                ", value=" + valueWhenPlaced +
                ", TL=" + TL +
                ", TR=" + TR +
                ", BL=" + BL +
                ", BR=" + BR ;
    } //TO-STRING METHOD

    @Override
    public String printCardRedably() {
        return "Resource Card-> id=" + id + ", value=" + valueWhenPlaced + ", type=" + type + ", Corners:\n     TL=" + TL + ", TR=" + TR+
                "\n     BL=" + BL + ", BR=" + BR ;
    }



    public JsonObject toJsonObject() {
        JsonObject jsonObject = super.toJsonObject(); // Call the parent class method to get common properties
        jsonObject.addProperty("cardType", "ResourceCard"); // Add cardType property
        return jsonObject;
    }

    public static ResourceCard fromJsonObject(JsonObject jsonObject) {
        Card card = fromJson(jsonObject); // Call the parent class method to get common properties

        return new ResourceCard(
                card.getId(),
                card.getType(),
                card.getValueWhenPlaced(),
                card.getTL(),
                card.getTR(),
                card.getBL(),
                card.getBR()
        );
    }
}