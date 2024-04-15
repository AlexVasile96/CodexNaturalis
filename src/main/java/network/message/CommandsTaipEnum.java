package network.message;

public enum CommandsTaipEnum {
    INFO,

    ERROR, //Signal for eventual errors

    //Server event signaling messages

    GAME_START, //Signals the beginning of the game

    WAIT_PLAYERS, //Signals the wait for more players to join

    CONFIRM_USERNAME, //Signals the registration of the client's username

    REACHED_20_POINTS, //signals the end of the game and the start of point counting

    GAME_END, //Signals the real end of the game

    PLAYER_CONNECTED, //Signals the connection of a player to the game

    PLAYER_DISCONNECTED, //Signals the disconnection of a player from the game

    //Client messages

    NICKNAME, //Player's nickname

    NUM_OF_PLAYERS, //The game's number of players

    COMMAND, //The player's game action command

    //General messages

    PING //The ping to verify connection
}
