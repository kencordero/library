package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Game {
    private int mId;
    private final UUID mUUID;
    private String mTitle;

    public Game() {
        this(0, "");
    }

    public Game(int id, String title) {
        mId = id;
        mTitle = title;
        mUUID = UUID.randomUUID();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
