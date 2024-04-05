package model;
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
