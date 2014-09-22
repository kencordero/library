package com.kentheken.library.models;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Platform {
    private final int mId;
    private final String mName;

    public Platform(int id, String name) {
        mId = id;
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }
}
