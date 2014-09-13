package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Game {
    public enum FLAG { NEW, MODIFIED, UNMODIFIED };

    private final UUID mId;
    private String mTitle;
    private FLAG mFlag;

    public Game() {
        this(UUID.randomUUID(), "", FLAG.NEW);
    }

    public Game(UUID id, String title, FLAG flag) {
        mTitle = title;
        mId = id;
        mFlag = flag;
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

    public FLAG getFlag() {
        return mFlag;
    }

    public void setFlag(FLAG flag) {
        mFlag = flag;
    }
}
