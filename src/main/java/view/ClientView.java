package view;

import model.game.Game;

//CLASSE CHE MANTIENE TUTTE LE INFORMAZIONI DEL SINGOLO CLIENT/PLAYER
public class ClientView {
    String userName=null;
    Game game;
    boolean waitPlayers=false;
    public String getUsername() {
        return userName;
    }
    public boolean getWaitPlayers() {
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isWaitPlayers() {
        return waitPlayers;
    }

    public void setUsername(String username) {
        this.userName = username;
    }
    public void setWaitPlayers(boolean waitPlayers) {
        this.waitPlayers = waitPlayers;
    }
}
