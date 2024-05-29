package model;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.ResourceUpdater;
import it.polimi.ingsw.model.game.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceUpdaterTest {
    //initialization
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0, Dot.GREEN,board);
    private ResourceUpdater resourceUpdater = new ResourceUpdater();

    @Test
    void testUpdatePlayerPoints() {
        //Creating the resource card having id 40, which gives 1 point when placed (not the actual game card!)
        Corner TL = new Corner(SpecificSeed.MUSHROOM,24,24,SpecificSeed.MUSHROOM);
        Corner TR = new Corner(SpecificSeed.EMPTY,24,25,SpecificSeed.MUSHROOM);
        Corner BL = new Corner(SpecificSeed.PLANT,25,24,SpecificSeed.MUSHROOM);
        Corner BR = new Corner(SpecificSeed.INSECT,25,25,SpecificSeed.MUSHROOM);
        ResourceCard resourceCard = new ResourceCard(40,SpecificSeed.INSECT,1,TL,TR,BL,BR);
        ResourceCard resourceCard2 = new ResourceCard(37,SpecificSeed.INSECT,0,TL,TR,BL,BR);

        //simulate the placement of the previously created card and call the method that should give
        // 1 point to the player

        resourceUpdater.updatePlayerPoints(resourceCard,player,board);
        resourceUpdater.updatePlayerPoints(resourceCard2,player,board);
        board.printBoard();
        Assertions.assertEquals(1,player.getPlayerScore());
    }
}