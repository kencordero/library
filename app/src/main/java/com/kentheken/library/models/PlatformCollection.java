package com.kentheken.library.models;

import android.content.Context;

import com.kentheken.library.utils.LibraryDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by kenneth on 9/20/14.
 */
public class PlatformCollection {
    private final Context mAppContext;
    private final ArrayList<Platform> mPlatforms;
    private String[] mPlatformList;
    private static PlatformCollection sCollection;
    private static final String DB_NAME = "library.db3";

    public static PlatformCollection get(Context c) {
        if (sCollection == null) {
            sCollection = new PlatformCollection(c.getApplicationContext());
        }
        return sCollection;
    }

    private PlatformCollection(Context appContext) {
        mAppContext = appContext;
        mPlatforms = LibraryDatabaseHelper.get(mAppContext).loadPlatforms();
    }

    public int getCount() {
        return mPlatforms.size();
    }

    public ArrayList<Platform> getPlatforms() {
        return mPlatforms;
    }

    public Platform getPlatform(int platformId) {
        for (Platform platform: mPlatforms) {
            if (platform.getId() == platformId)
                return platform;
        }
        return null;
    }

    public long getPlatformId(String platformName) {
        for (Platform platform: mPlatforms) {
            if (platform.getName().equals(platformName))
                return platform.getId();
        }
        return -1;
    }

    public Platform get(int idx) {
        return mPlatforms.get(idx);
    }

    public String[] getPlatformList() {
        if (mPlatformList == null) {
            mPlatformList = new String[mPlatforms.size()];
            int platformIdx = 0;
            for (Platform platform : mPlatforms) {
                mPlatformList[platformIdx++] = platform.getName();
            }
        }
        return mPlatformList;
    }
}
