package model;

import java.util.Set;

public class Game implements WhatCanPlayerDo{
    private Player player;
    private String username;

    public Game(Set<String> playerName){
    }

    public Player getCurrentPlayer() {
        return player;
    }
    public String getUsername()
    {
        return username;
    }

       /* playcard(chosencard)
        {
            currentplayer.playCard();
        }*/

}

