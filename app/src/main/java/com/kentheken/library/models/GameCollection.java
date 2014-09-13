package com.kentheken.library.models;

import android.content.Context;

import com.kentheken.library.LibraryDatabaseHelper;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by kcordero on 6/18/2014.
 */
public class GameCollection {
    private final Context mAppContext;
    private ArrayList<Game> mGames;
    private static GameCollection sCollection;
    private static final String DB_NAME = "library.db3";
    private LibraryDatabaseHelper mHelper;

    public static GameCollection get(Context c) {
        if (sCollection == null) {
            sCollection = new GameCollection(c.getApplicationContext());
        }
        return sCollection;
    }

    private GameCollection(Context appContext) {
        mAppContext = appContext;
        mGames = new ArrayList<Game>();
        mHelper = new LibraryDatabaseHelper(mAppContext, DB_NAME);
        mGames = mHelper.loadGames();
    }

    public int getItemCount() {
        return mGames.size();
    }

    public ArrayList<Game> getGames() {
        return mGames;
    }

    public void saveGame(Game game) {
        mHelper.saveGame(game);
    }

    public Game getGame(UUID gameId) {
        for (Game game: mGames) {
            if (game.getId() == gameId)
                return game;
        }
        return null;
    }

    public Game get(int idx) {
        return mGames.get(idx);
    }

    public void addGame(Game game) {
        mGames.add(game);
    }
}
