package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Game {
    private final String mId;
    private final String mTitle;
    private Platform mPlatform;

    public Game(String title) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }
}
