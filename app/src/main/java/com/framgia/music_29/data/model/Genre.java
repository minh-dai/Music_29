package com.framgia.music_29.data.model;

import java.util.List;

public class Genre {
    private String mName;
    private List<Song> mList;
    private String url;

    public Genre(String name, String url) {
        mName = name;
        this.url = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Song> getList() {
        return mList;
    }

    public void setList(List<Song> list) {
        mList = list;
    }

}
