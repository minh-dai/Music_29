package com.framgia.music_29.data.model;

import java.util.List;

public class Genre {
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
}
