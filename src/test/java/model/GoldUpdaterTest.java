package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
//Test del comportamento della classe quando il player piazza la carta con id=41 la quale
//da 1 punto per ogni piuma
class GoldUpdaterTest {
    //INIZIALIZZAZIONI
    private Board board = new Board(50,50);
    private Player player = new Player("Calla",0,Dot.GREEN,board);
    private Node node3 = new Node(SpecificSeed.FEATHER,2,2);
    private GoldUpdater goldUpdater = new GoldUpdater();

    @Test
    void updatePlayerPoints() {
        //CREAZIONE DELLA CARTA GOLD CON ID=41
        Corner TL = new Corner(SpecificSeed.MUSHROOM,24,24);
        Corner TR = new Corner(SpecificSeed.EMPTY,24,25);
        Corner BL = new Corner(SpecificSeed.PLANT,25,24);
        Corner BR = new Corner(SpecificSeed.INSECT,25,25);
        List<SpecificSeed> attributi = new ArrayList<SpecificSeed>();
        attributi.add(SpecificSeed.MUSHROOM);
        attributi.add(SpecificSeed.MUSHROOM);
        attributi.add(SpecificSeed.ANIMAL);
        GoldCard goldCard = new GoldCard(41,SpecificSeed.MUSHROOM,1,SpecificSeed.FEATHER,TL,TR,BL,BR,attributi);

        //ASSEGNO A 4 NODI LO SPECIFIC SEED NECESSARIO A FAR GUADAGNARE PUNTI AL GIOCATORE
        for(int i=0; i<2; i++) {
            for (int j = 0; j < 2; j++) {
                board.setNode(j, i, node3);
            }
        }
        //CHIAMO IL METODO, AVENDO 4 NODI DELLO SPECIFIC SEED NECESSARIO A FAR GUADAGNARE PUNTI, MI ASPETTO CHE IL PLAYER GUADAGNI 4 PUNTI, 1 per nodo
        goldUpdater.updatePlayerPoints(goldCard,player,board);
        int x = player.getPlayerScore();
        Assertions.assertEquals(4,x);

    }
}