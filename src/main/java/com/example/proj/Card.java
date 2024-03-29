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


        public Card (int id, SpecificSeed type, int value, Corner TL, Corner TR,Corner BL, Corner BR){ //Card constructor
            this.id=id;
            this.type=type;
            this.valueWhenPlaced=value;
            this.TL=TL;
            this.TR=TR;
            this.BL=BL;
            this.BR=BR;
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
                    ", BR=" + BR;    }


        //Different Getters
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

}
