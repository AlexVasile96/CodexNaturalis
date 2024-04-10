package observer;

public interface Observer {

    void update(Object observable);
    void updateSinglePlayer(String username);
}