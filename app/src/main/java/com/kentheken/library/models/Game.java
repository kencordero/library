package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Game {
    public static class Flag {
        public static final int NEW = 0;
        public static final int MODIFIED = 1;
        public static final int UNMODIFIED = 2;
    }

    private final UUID mUuid;
    private int mId;
    private String mTitle;
    private int mFlag;
    private boolean[] mPlatformSelections;

    public Game() {
        this(UUID.randomUUID(), "", Flag.NEW, 0);
    }

    public Game(UUID uuid, String title, int flag, int id) {
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

    public void setId(int id) {
        mId = id;
    }

    public int getFlag() {
        return mFlag;
    }

    public void setFlag(int flag) {
        mFlag = flag;
    }

    public boolean[] getPlatformSelections() {
        return mPlatformSelections;
    }

    public void setPlatformSelections(boolean[] selections) {
        mPlatformSelections = selections;
    }
}
