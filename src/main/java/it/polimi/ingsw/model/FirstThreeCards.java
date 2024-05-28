package it.polimi.ingsw.model;

import it.polimi.ingsw.model.deck.GoldDeck;
import it.polimi.ingsw.model.deck.ResourceDeck;
import it.polimi.ingsw.model.game.Player;

public class FirstThreeCards {
    private Player player;
    private ResourceDeck resourceDeck;
    private GoldDeck goldDeck;

    public  FirstThreeCards(Player player, ResourceDeck resourceDeck, GoldDeck goldDeck)
    {
        this.player=player;
        this.resourceDeck=resourceDeck;
        this.goldDeck= goldDeck;
    }
    public void yourThreeCards()
    {
        player.drawResourceCard(resourceDeck);
        player.drawResourceCard(resourceDeck);
        player.drawGoldCard(goldDeck);
    }
}
