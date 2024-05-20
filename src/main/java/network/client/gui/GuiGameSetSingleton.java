package network.client.gui;

public class GuiGameSetSingleton {
    private static GuiGameSetSingleton instance;
    private boolean gameSizeSet;

    private GuiGameSetSingleton() {

        gameSizeSet = false;
    }

    public static GuiGameSetSingleton getInstance() {
        if (instance == null) {
            instance = new GuiGameSetSingleton();
        }
        return instance;
    }

    public boolean isGameSizeSet() {
        return gameSizeSet;
    }

    public void setGameSizeSet(boolean gameSizeSet) {
        this.gameSizeSet = gameSizeSet;
    }
}