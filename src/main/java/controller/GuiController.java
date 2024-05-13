package controller;

import model.game.Player;

public class GuiController {
    private static GuiController instance=null;
    private int numOfPlayersLogged;
    private int gameSize;
    private boolean sizeSet;
    private Player currentPlayer;
    private GuiController() {

    }

    public static synchronized GuiController getInstance() {
        if (instance == null) {
            instance = new GuiController();
        }
        return instance;
    }

    public boolean isSizeSet() {
        return sizeSet;
    }

    public void setSizeSet(boolean sizeSet) {
        this.sizeSet = sizeSet;
    }

    public int getNumOfPlayersLogged() {
        return numOfPlayersLogged;
    }

    public void setNumOfPlayersLogged(int numOfPlayersLogged) {
        this.numOfPlayersLogged = numOfPlayersLogged;
    }

    public int getGameSize() {
        return gameSize;
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public static void setInstance(GuiController instance) {
        GuiController.instance = instance;
    }
}
