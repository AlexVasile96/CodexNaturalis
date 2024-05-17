package model.card;


import com.google.gson.JsonObject;
import model.game.Corner;
import model.game.Node;
import model.game.SpecificSeed;
import org.json.JSONObject;

public class ResourceCard extends Card{
    private Node node;
    public ResourceCard(int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR) {
        super(id, type, value, TL, TR, BL, BR);
        Corner TLBack = new Corner(SpecificSeed.EMPTY, 0, 0,type); //intellij suggetion, first it was declared as attribute
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



    public JsonObject toJsonObject(){
        JsonObject jsonObject= new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("specificSeedType", type.ordinal());
        jsonObject.addProperty("value", valueWhenPlaced);
        jsonObject.addProperty("TopLeftCorner", String.valueOf(TL));
        jsonObject.addProperty("TopRightCorner", String.valueOf(TR));
        jsonObject.addProperty("BottomLeftCorner", String.valueOf(BL));
        jsonObject.addProperty("BottomRightCorner", String.valueOf(BR));
        return jsonObject;

        }
    }