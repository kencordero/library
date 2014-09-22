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
        mHelper = LibraryDatabaseHelper.get(mAppContext, DB_NAME);
        mGames = mHelper.loadGames();
    }

    public boolean[] getPlatformSelections(Game game) {
        if (game.getPlatformSelections() == null) {
            ArrayList<Platform> platforms = PlatformCollection.get(mAppContext).getPlatforms();
            boolean[] selections = new boolean[platforms.size()];
            ArrayList<Integer> gamePlatformIds = mHelper.getGamePlatformIDs(game);
            for (Platform platform : platforms) {

            }
            game.setPlatformSelections(selections);
        }
        return game.getPlatformSelections();
    }

    public int getCount() {
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
            if (game.getUuid() == gameId)
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
