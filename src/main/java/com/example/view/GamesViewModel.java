package com.example.view;

import com.example.database.Game;

import java.util.List;

public class GamesViewModel {

    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

}
