package model;

import model.card.ResourceCard;
import model.card.ResourceUpdater;
import model.game.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ResourceUpdaterTest {
    //INIZIALIZZAZIONI
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0, Dot.GREEN,board);
    private ResourceUpdater resourceUpdater = new ResourceUpdater();

    @Test
    void testUpdatePlayerPoints() {
        //CREAZIONE DELLA CARTA RISORSA CON ID = 40, LA QUALE DA 1 PUNTO QUANDO VIENE PIAZZATA (VALUE=1)
        Corner TL = new Corner(SpecificSeed.MUSHROOM,24,24,SpecificSeed.MUSHROOM);
        Corner TR = new Corner(SpecificSeed.EMPTY,24,25,SpecificSeed.MUSHROOM);
        Corner BL = new Corner(SpecificSeed.PLANT,25,24,SpecificSeed.MUSHROOM);
        Corner BR = new Corner(SpecificSeed.INSECT,25,25,SpecificSeed.MUSHROOM);
        List<SpecificSeed> attributi = new ArrayList<SpecificSeed>();
        attributi.add(SpecificSeed.MUSHROOM);
        attributi.add(SpecificSeed.MUSHROOM);
        attributi.add(SpecificSeed.ANIMAL);
        ResourceCard resourceCard = new ResourceCard(40,SpecificSeed.INSECT,1,TL,TR,BL,BR);

        //SIMULO IL PIAZZAMENTO DELLA CARTA RISORSA DI CUI SOPRA
        //E CHIAMO IL METODO CHE DOVREBBE DARMI UN PUNTO
       resourceUpdater.updatePlayerPoints(resourceCard,player,board);
        int x = player.getPlayerScore();
        Assertions.assertEquals(1,x);
    }
}