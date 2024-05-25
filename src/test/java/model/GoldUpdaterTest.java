package model;

import model.card.GoldCard;
import model.card.GoldUpdater;
import model.game.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
//Testing the behaviour of the class when the player places a card (id=41) which gives 1 point foreach FEATHER on the board

class GoldUpdaterTest {
    //INITIALIZATIONS
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0,Dot.GREEN,board);
    private Node node3 = new Node(SpecificSeed.FEATHER,2,2);
    private GoldUpdater goldUpdater = new GoldUpdater();

    @Test
    void updatePlayerPoints() {
        //CREATING GOLD CARD (ID=41)
        Corner TL = new Corner(SpecificSeed.MUSHROOM,24,24,SpecificSeed.MUSHROOM);
        Corner TR = new Corner(SpecificSeed.EMPTY,24,25,SpecificSeed.MUSHROOM);
        Corner BL = new Corner(SpecificSeed.PLANT,25,24,SpecificSeed.MUSHROOM);
        Corner BR = new Corner(SpecificSeed.INSECT,25,25,SpecificSeed.MUSHROOM);
        List<SpecificSeed> attributes = new ArrayList<SpecificSeed>();
        attributes.add(SpecificSeed.MUSHROOM);
        attributes.add(SpecificSeed.MUSHROOM);
        attributes.add(SpecificSeed.ANIMAL);
        GoldCard goldCard = new GoldCard(41,SpecificSeed.MUSHROOM,1,SpecificSeed.FEATHER,TL,TR,BL,BR,attributes);

        //placing the values necessary to satisfy the requirements of the card
        for(int i=0; i<2; i++) {
            for (int j = 0; j < 2; j++) {
                board.setNode(j, i, node3);
            }
        }
        //the player should get 4 points (1 foreach FEATHER)
        goldUpdater.updatePlayerPoints(goldCard,player,board);
        int x = player.getPlayerScore();
        Assertions.assertEquals(4,x);

    }
}