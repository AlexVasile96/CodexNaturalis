package view;

import model.Game;

//CLASSE CHE MANTIENE TUTTE LE INFORMAZIONI DEL SINGOLO CLIENT/PLAYER
public class ClientView {
    String userName=null;
    boolean waitPlayers=false;
    public String  getUsername() {
        return null;
    }

    public Game getGame() {
        return null;
    }

    public boolean getWaitPlayers() {
        return false;
    }

    public void setUsername(String username) {
        this.userName = username;
    }
    public void setWaitPlayers(boolean waitPlayers) {
        this.waitPlayers = waitPlayers;
    }
}
