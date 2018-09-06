package com.framgia.music_29.data.source;

import com.framgia.music_29.data.model.Genre;

public interface SongDataSource {

    interface CallBack {
        void onDataLoaded(Genre genre);

        void onDataError(Exception e);
    }

    void getDatas(Genre genre, CallBack callBack);
}
