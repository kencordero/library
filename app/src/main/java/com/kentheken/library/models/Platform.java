package com.kentheken.library.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kcordero on 6/17/2014.
 */
public class Platform implements Parcelable {
    private final int mId;
    private final String mName;

    public Platform(int id, String name) {
        mId = id;
        mName = name;
    }

    protected Platform(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
    }

    public static final Creator<Platform> CREATOR = new Creator<Platform>() {
        @Override
        public Platform createFromParcel(Parcel in) {
            return new Platform(in);
        }

        @Override
        public Platform[] newArray(int size) {
            return new Platform[size];
        }
    };

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
    }
}
