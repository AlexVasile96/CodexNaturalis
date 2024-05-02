package controller;

import model.game.Player;

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

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /*NON FUNZIONA CON PIU DI 2 GIOCATORI
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }*/

    public void nextTurn() {
        if(currentPlayerIndex < players.size()-1) currentPlayerIndex++;
        else currentPlayerIndex = 0;
    }

    @Override
    public String toString() {
        return "TurnController{" +
                "players=" + players +
                ", currentPlayerIndex=" + currentPlayerIndex +
                '}';
    }
}
