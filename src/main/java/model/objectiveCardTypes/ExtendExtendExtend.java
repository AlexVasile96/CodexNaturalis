package model.objectiveCardTypes;

import model.Board;
import model.Player;
import model.SpecificSeed;

public interface ExtendExtendExtend { //   INTERFACED IMPLEMENTED BY THE SPECIFIC OBJECTIVE CARD TYPE
    public boolean checkPattern(Board board, SpecificSeed seed, Player player);
    public boolean checkPattern(Board board, SpecificSeed seed, Player player, SpecificSeed specificSeed);
}
