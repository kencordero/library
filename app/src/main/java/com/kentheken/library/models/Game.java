package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Game {
    public enum FLAG { NEW, MODIFIED, UNMODIFIED }

    private final UUID mUuid;
    private int mId;
    private String mTitle;
    private FLAG mFlag;
    private boolean[] mPlatformSelections;

    public Game() {
        this(UUID.randomUUID(), "", FLAG.NEW, 0);
    }

    public Game(UUID uuid, String title, FLAG flag, int id) {
        mTitle = title;
        mUuid = uuid;
        mFlag = flag;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getUuid() {
        return mUuid;
    }

    public int getId() { return mId; }

    public void setId(int id) {
        mId = id;
    }

    public FLAG getFlag() {
        return mFlag;
    }

    public void setFlag(FLAG flag) {
        mFlag = flag;
    }

    public boolean[] getPlatformSelections() {
        return mPlatformSelections;
    }

    public void setPlatformSelections(boolean[] selections) {
        mPlatformSelections = selections;
    }
}
