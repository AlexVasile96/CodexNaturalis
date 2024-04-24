package server;

import view.ClientView;

import java.util.HashMap;
import java.util.Map;

public class ClientManager {
    private Map<String, ClientView> clientViews;

    public ClientManager() {
        this.clientViews = new HashMap<>();
    }

    public void addClientView(String username, ClientView clientView) {
        clientViews.put(username, clientView);
    }

    public ClientView getClientView(String username) {
        return clientViews.get(username);
    }

}