package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.game.Corner;
import it.polimi.ingsw.model.game.Node;
import it.polimi.ingsw.model.game.SpecificSeed;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Card {
        protected int id; //id which identifies the specific card
        protected SpecificSeed type; //specificCardType
        protected int valueWhenPlaced; //value to the player_score when placed
        protected Corner TL; //TopLeftCorner
        protected Corner TR; //TopRightCorner
        protected Corner BL; //BottomLeftCorner
        protected Corner BR; //BottomRightCorner
        private Corner TLBack;
        private Corner TRBack;
        private Corner BLBack;
        private Corner BRBack;
        private int indexOnTheBoard;
        private Node node;
        private boolean isCardBack;
        private Corner TLIBack;
        private Corner TRIBack;
        private Corner BLIBack;
        private Corner BRIBack;

    public Card (int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR){ //Card constructor
            this.id=id;
            this.type=type;
            this.valueWhenPlaced=value;
            this.TL=TL;
            this.TR=TR;
            this.BL=BL;
            this.BR=BR;
            this.isCardBack=false;
            this.TLBack = new Corner(SpecificSeed.EMPTY,0,0,type);
            this.TRBack = new Corner(SpecificSeed.EMPTY,0,0,type);
            this.BLBack = new Corner(SpecificSeed.EMPTY,0,0,type);
            this.BRBack = new Corner(SpecificSeed.EMPTY,0,0,type);
            //BACKUP-ORIGINAL CORNERS
            this.TLBack.setSpecificCornerSeed(TL.getSpecificCornerSeed(),type);
            this.TRBack.setSpecificCornerSeed(TR.getSpecificCornerSeed(),type);
            this.BLBack.setSpecificCornerSeed(BL.getSpecificCornerSeed(),type);
            this.BRBack.setSpecificCornerSeed(BR.getSpecificCornerSeed(),type);
        }

        @Override
        public String toString() {
            return "Card->" +
                    " id=" + id +
                    ", type=" + type +
                    ", value=" + valueWhenPlaced +
                    ", TL=" + TL +
                    ", TR=" + TR +
                    ", BL=" + BL +
                    ", BR=" + BR ;
        } //TO-STRING METHOD


        //GETTER AND SETTER

        public int getId() {
            return id;
        }
        public SpecificSeed getType() {
            return type;
        }
        public int getValueWhenPlaced() {
            return valueWhenPlaced;
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
        public void setId(int id) {
            this.id = id;
        }
        public void setType(SpecificSeed type) {
            this.type = type;
        }

        public void setTL(Corner TL) {
            this.TL = TL;
        }
        public void setTR(Corner TR) {
            this.TR = TR;
        }
        public void setBL(Corner BL) {
            this.BL = BL;
        }
        public void setBR(Corner BR) {
            this.BR = BR;
        }
        public void setIndexOnTheBoard(int indexOnTheBoard) {
            this.indexOnTheBoard = indexOnTheBoard;
        }
        public int getIndexOnTheBoard() {
            return indexOnTheBoard;
        }
        public Node getNode() {
            return node;
        }
        public void setNode(Node node) {
            this.node = node;
        }
        public boolean isCardBack() {
            return isCardBack;
        }
        public void setCardBack(boolean cardBack) {
            isCardBack = cardBack;
        }
        public Corner getTLBack() {
            return TLBack;
        }
        public void setTLBack(Corner TLBack) {
            this.TLBack = TLBack;
        }
        public Corner getTRBack() {
            return TRBack;
        }
        public void setTRBack(Corner TRBack) {
            this.TRBack = TRBack;
        }
        public Corner getBLBack() {
            return BLBack;
        }
        public void setBLBack(Corner BLBack) {
            this.BLBack = BLBack;
        }
        public Corner getBRBack() {
            return BRBack;
        }
        public void setBRBack(Corner BRBack) {
            this.BRBack = BRBack;
        }

        public List<SpecificSeed> getAttributes(){
            List<SpecificSeed> temp = new ArrayList<>();
            temp.add(type);
            return temp;
        }
    public static Card fromJson(JsonObject jsonObject) {
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
        Card card = new Card(id, type, value, tl, tr, bl, br);
        card.setTLBack(tlBack);
        card.setTRBack(trBack);
        card.setBLBack(blBack);
        card.setBRBack(brBack);


        return card;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("type", type.toString());
        jsonObject.addProperty("value", valueWhenPlaced);
        jsonObject.add("TL", TL.toJsonObject());
        jsonObject.add("TR", TR.toJsonObject());
        jsonObject.add("BL", BL.toJsonObject());
        jsonObject.add("BR", BR.toJsonObject());
        jsonObject.add("TLBack", TLBack.toJsonObject());
        jsonObject.add("TRBack", TRBack.toJsonObject());
        jsonObject.add("BLBack", BLBack.toJsonObject());
        jsonObject.add("BRBack", BRBack.toJsonObject());
        jsonObject.addProperty("cardType", "Card");
        return jsonObject;
    }

}


