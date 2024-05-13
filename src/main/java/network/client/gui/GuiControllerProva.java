package network.client.gui;

public class GuiControllerProva {
    private int numberOfPlayersWaiting = 0;

    public int getNumberOfPlayersWaiting() {
        return numberOfPlayersWaiting;
    }

    public void addPlayerToWaitingList() {
        numberOfPlayersWaiting++;
    }
}
