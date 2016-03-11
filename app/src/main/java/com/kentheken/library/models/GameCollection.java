package com.kentheken.library.models;

import android.content.Context;

import com.kentheken.library.utils.LibraryDatabaseHelper;

import java.util.ArrayList;
import java.util.UUID;

public class GameCollection {
    private final Context mAppContext;
    private ArrayList<Game> mGames;
    private static GameCollection sCollection;
    private LibraryDatabaseHelper mHelper;

    public static GameCollection get(Context c) {
        if (sCollection == null) {
            sCollection = new GameCollection(c.getApplicationContext());
        }
        return sCollection;
    }

    private GameCollection(Context appContext) {
        mAppContext = appContext;
        getAllGames();
    }

    public void getAllGames() {
        mGames = new ArrayList<>();
        mHelper = LibraryDatabaseHelper.get(mAppContext);
        mGames = mHelper.getAllGames();
    }

    public ArrayList<Game> getGamesFilteredByPlatform(long platformId) {
        mGames = new ArrayList<>();
        //TODO
        return mGames;
    }

    public boolean[] getPlatformSelections(Game game) {
        if (game.getPlatformSelections() == null) {
            ArrayList<Platform> platforms = PlatformCollection.get(mAppContext).getPlatforms();
            boolean[] selections = new boolean[platforms.size()];
            ArrayList<Integer> gamePlatformIds = mHelper.getGamePlatformIDs(game);
            int idx = 0;
            for (Platform platform : platforms) {
                selections[idx++] = gamePlatformIds.contains(platform.getId());
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

    public Game getGame(UUID gameUuid) {
        for (Game game: mGames) {
            if (game.getUuid() == gameUuid)
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
