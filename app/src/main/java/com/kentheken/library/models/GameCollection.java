package com.kentheken.library.models;

import android.content.Context;

import java.util.ArrayList;

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

    public void addItem(int gameId, String gameText) {
        mGames.add(new Game(gameId, gameText));
    }

    public ArrayList<Game> getGames() {
        return mGames;
    }
}
