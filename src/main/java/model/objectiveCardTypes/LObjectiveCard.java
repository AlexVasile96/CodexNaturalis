package model.objectiveCardTypes;
import model.game.Board;
import model.game.Node;
import model.game.Player;
import model.game.SpecificSeed;

public class LObjectiveCard implements ExtendExtendExtend {

    public boolean checkColumnPattern(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2) {
        //seed1-> single card
        //seed 2-> 2 cards
       if(seed1==SpecificSeed.PLANT && seed2==SpecificSeed.MUSHROOM){
           onePlantAndTwoMushrooms(board,player,seed1,seed2);
       }
       else if(seed1==SpecificSeed.MUSHROOM && seed2==SpecificSeed.ANIMAL){
           oneMushAndTwoPlants(board,player,seed1,seed2);
       }
       else if(seed1== SpecificSeed.ANIMAL && seed2==SpecificSeed.INSECT)
       {
           oneAnimalAndTwoInsects(board,player,seed1,seed2);
       }
       else if(seed1==SpecificSeed.INSECT&& seed2==SpecificSeed.PLANT)
       {
           oneInsectAndTwoPlants(board,player,seed1,seed2);
       }
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

    public boolean onePlantAndTwoMushrooms(Board board, Player player,SpecificSeed seed1, SpecificSeed seed2){
       OnePlantAndTwoMushrooms onePlantAndTwoMushrooms= new OnePlantAndTwoMushrooms();
       onePlantAndTwoMushrooms.realCheck(board,player,seed1,seed2);
       return true;
    }
    private void oneMushAndTwoPlants(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        OneMushAndTwoPlants oneMushAndTwoPlants= new OneMushAndTwoPlants();
        oneMushAndTwoPlants.realCheck(board,player,seed1,seed2);
    }
    private void oneAnimalAndTwoInsects(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        OneAnimalAndTwoInsects oneAnimalAndTwoInsects= new OneAnimalAndTwoInsects();
        oneAnimalAndTwoInsects.realCheck(board, player, seed1, seed2);
    }

    private void oneInsectAndTwoPlants(Board board, Player player, SpecificSeed seed1, SpecificSeed seed2){
        OneInsectAndTwoPlants oneInsectAndTwoPlants= new OneInsectAndTwoPlants();
        oneInsectAndTwoPlants.realCheck(board, player, seed1, seed2);
    }

}
