package com.example.proj;

public class Card {
        private int id; //id which identifies the specific card
        private SpecificSeed type; //specificCardType
        private int valueWhenPlaced; //value to the player_score when placed
        private Corner TL; //TopLeftCorner
        private Corner TR; //TopRightCorner
        private Corner BL; //BottomLeftCorner
        private Corner BR; //BottomRightCorner

        public Card (int id, SpecificSeed type, int value, Corner TL, Corner TR,Corner BL, Corner BR){ //Constructor
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
            return "Card{" +
                    "id=" + id +
                    ", type=" + type +
                    ", value=" + valueWhenPlaced +
                    ", TL=" + TL +
                    ", TR=" + TR +
                    ", BL=" + BL +
                    ", BR=" + BR +
                    '}';    }


        //Different Getter
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
