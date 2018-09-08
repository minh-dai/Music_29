package com.framgia.music_29.data.source.local;

import android.content.ContentResolver;
import com.framgia.music_29.data.source.SongDataSource;

public class SongLocalDataSoure {
    private static SongLocalDataSoure sInstance;

    public static SongLocalDataSoure getInstance() {
        if (sInstance == null) {
            sInstance = new SongLocalDataSoure();
        }
        return sInstance;
    }

    public void loadDataLocal(ContentResolver contentResolver,
            SongDataSource.LocalCallBack callBack) {
        new GetDataLocal(contentResolver, callBack).execute();
    }
}
