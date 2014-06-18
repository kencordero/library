package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Game {
    private final int mId;
    private final String mTitle;

    public Game(int id, String title) {
        mId = id;
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public Integer getId() {
        return mId;
    }
}
