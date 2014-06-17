package com.kentheken.library.models;

import java.util.UUID;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Platform {
    private final String mId;
    private final String mName;

    public Platform(String name) {
        mId = UUID.randomUUID().toString();
        mName = name;
    }

    public String getName() {
        return mName;
    }
}
