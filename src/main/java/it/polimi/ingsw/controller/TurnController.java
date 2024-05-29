package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.Player;

import java.util.List;

public class TurnController {
    private List<Player> players;
    private int currentPlayerIndex;

    public TurnController(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public void setCurrentPlayer(Player player) {
        if (players.contains(player)) {
            this.currentPlayerIndex = players.indexOf(player);
        } else {

            throw new IllegalArgumentException("Player not found in the list");
        }
    }

    @Override
    public String toString() {
        return "TurnController{" +
                "players=" + players +
                ", currentPlayerIndex=" + currentPlayerIndex +
                '}';
    }
}
