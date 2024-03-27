package com.example.proj;

public class Card {

        private int id;
        private SpecificSeed type;
        private int valueWhenPlaced;
        private Corner TL;
        private Corner TR;
        private Corner BL;
        private Corner BR;

        public Card (int id, SpecificSeed type, int value, Corner TL, Corner TR,Corner BL, Corner BR){
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
