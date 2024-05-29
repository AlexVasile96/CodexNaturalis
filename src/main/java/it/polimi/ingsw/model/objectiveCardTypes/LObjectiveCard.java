package it.polimi.ingsw.model.objectiveCardTypes;
import it.polimi.ingsw.model.game.Board;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.SpecificSeed;

public class LObjectiveCard implements ExtendExtendExtend {

    @Override
    public boolean checkPattern(Board board, SpecificSeed seed, Player player) {
return false;
    }

    public boolean onePlantAndTwoMushrooms(Board board, Player player,SpecificSeed seed1, SpecificSeed seed2){
       OnePlantAndTwoMushrooms onePlantAndTwoMushrooms= new OnePlantAndTwoMushrooms();
       onePlantAndTwoMushrooms.realCheck(board,player,seed1,seed2);
       return true;
    }
    public boolean oneMushAndTwoAnimals(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        OneMushAndTwoAnimals oneMushAndTwoAnimals = new OneMushAndTwoAnimals();
        oneMushAndTwoAnimals.realCheck(board,player,seed1,seed2);
        return true;
    }
    public boolean oneAnimalAndTwoInsects(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        OneAnimalAndTwoInsects oneAnimalAndTwoInsects= new OneAnimalAndTwoInsects();
        oneAnimalAndTwoInsects.realCheck(board, player, seed1, seed2);
        return true;
    }

    public boolean oneInsectAndTwoPlants(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        OneInsectAndTwoPlants oneInsectAndTwoPlants= new OneInsectAndTwoPlants();
        oneInsectAndTwoPlants.realCheck(board, player, seed1, seed2);
        return true;
    }

}
