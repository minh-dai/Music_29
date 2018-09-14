package com.framgia.music_29.data.source;

import android.content.ContentResolver;
import android.content.Context;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.model.Song;
import java.util.List;

public interface SongDataSource {

    interface CallBack {
        void onDataLoaded(Genre genre);

        void onDataError(Exception e);
    }

    interface LocalCallBack {
        void onDataLoaded(List<Song> songs);

        void onDataError();
    }

    void getDatas(Genre genre, CallBack callBack);

    void getSongs(Genre genre, CallBack callBack);

    void getDataLocal(ContentResolver contentResolver, LocalCallBack callback);

}
