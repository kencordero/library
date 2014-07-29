package com.kentheken.library.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kcordero on 6/18/2014.
 */
public class GameCollection {
    private final Context mAppContext;
    private ArrayList<Game> mGames;
    private static GameCollection sCollection;

    public static GameCollection get(Context c) {
        if (sCollection == null) {
            sCollection = new GameCollection(c.getApplicationContext());
        }
        return sCollection;
    }

    private GameCollection(Context appContext) {
        mAppContext = appContext;
        mGames = new ArrayList<Game>();
    }

    public int getItemCount() {
        return mGames.size();
    }

    public ArrayList<Game> getGames() {
        return mGames;
    }

    public void saveGames() {

    }

    public Game getGame(UUID gameId) {
        for (Game game: mGames) {
            if (game.getUUID() == gameId)
                return game;
        }
        return null;
    }

    public Game get(int idx) {
        return mGames.get(idx);
    }

    public void addGame(Game game) {
        game.setId(getItemCount());
        mGames.add(game);
    }

    public void addGame(int gameId, String gameTitle) {

    }
}
