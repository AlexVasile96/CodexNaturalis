package controller;

public class GuiController {
    private int numOfPlayersLogged;
    private static boolean isSizeSet;


    public GuiController(int numOfPlayersLogged) {
        this.numOfPlayersLogged = numOfPlayersLogged;
        isSizeSet = false;
    }

    public int getNumOfPlayersLogged() {
        return numOfPlayersLogged;
    }

    public void setNumOfPlayersLogged(int numOfPlayersLogged) {
        this.numOfPlayersLogged = numOfPlayersLogged;
    }

    public boolean isSizeSet() {
        return isSizeSet;
    }

    public void setSizeSet(boolean sizeSet) {
        isSizeSet = sizeSet;
    }
}
