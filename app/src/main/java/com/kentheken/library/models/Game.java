package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Game {
    private final UUID mId;
    private String mTitle;

    public Game() {
        this(UUID.randomUUID(), "");
    }

    public Game(UUID id, String title) {
        mTitle = title;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }
}
