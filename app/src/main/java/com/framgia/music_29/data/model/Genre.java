package com.framgia.music_29.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Genre implements Parcelable {
    private String mName;
    private List<Song> mSongs;
    private String mUrl;

    public Genre() {
    }

    public Genre(String url) {
        mUrl = url;
    }

    public Genre(String name, String url) {
        mName = name;
        mUrl = url;
    }

    protected Genre(Parcel in) {
        mName = in.readString();
        mSongs = in.createTypedArrayList(Song.CREATOR);
        mUrl = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public List<Song> getSongs() {
        return mSongs;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeTypedList(mSongs);
        dest.writeString(mUrl);
    }
}
