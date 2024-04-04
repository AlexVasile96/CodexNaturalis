package com.example.proj;

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
        private SpecificSeed center;

    public Card (int id, SpecificSeed type, int value, Corner TL, Corner TR, Corner BL, Corner BR){ //Card constructor
            this.id=id;
            this.type=type;
            this.valueWhenPlaced=value;
            this.TL=TL;
            this.TR=TR;
            this.BL=BL;
            this.BR=BR;
            this.center=type;
            this.isCardBack=false;
            this.TLBack = new Corner(SpecificSeed.EMPTY,0,0);
            this.TRBack = new Corner(SpecificSeed.EMPTY,0,0);
            this.BLBack = new Corner(SpecificSeed.EMPTY,0,0);
            this.BRBack = new Corner(SpecificSeed.EMPTY,0,0);
            //BACKUPORIGINAL CORNERS
            this.TLBack.setSpecificCornerSeed(TL.getSpecificCornerSeed());
            this.TRBack.setSpecificCornerSeed(TR.getSpecificCornerSeed());
            this.BLBack.setSpecificCornerSeed(BL.getSpecificCornerSeed());
            this.BRBack.setSpecificCornerSeed(BR.getSpecificCornerSeed());
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
                    ", BR=" + BR +
                    ", Center=" + center;
    } //TOSTRING METHOD


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
        public void setValueWhenPlaced(int valueWhenPlaced) {
            this.valueWhenPlaced = valueWhenPlaced;
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

}


