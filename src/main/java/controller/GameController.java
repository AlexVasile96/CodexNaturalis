package controller;

public class GameController {
    public boolean isGameStarted() {
        return this.gameState != GameState.LOGIN;
    }
}
