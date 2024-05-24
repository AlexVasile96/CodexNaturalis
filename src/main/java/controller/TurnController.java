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

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public synchronized void removePlayer(Player player) {
        nextTurn();
        int playerIndex = players.indexOf(player);
        if (playerIndex == -1) {
            return; // Il giocatore non è nella lista
        }
        players.remove(player);
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0; // Reset dell'indice se l'indice corrente è fuori dai limiti
        } else if (playerIndex < currentPlayerIndex) {
            currentPlayerIndex--; // Aggiorna l'indice del giocatore corrente se il giocatore rimosso è prima del giocatore corrente
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
