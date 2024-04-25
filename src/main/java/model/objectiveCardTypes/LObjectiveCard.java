package model.objectiveCardTypes;
import model.game.Board;
import model.game.Node;
import model.game.Player;
import model.game.SpecificSeed;

public class LObjectiveCard implements ExtendExtendExtend {
    public boolean checkColumnPattern(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2) {
    return true;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
        return false;
    }

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed) {
        return false;
    }
}
