package com.framgia.music_29.data.source.local;

import android.content.Context;
import com.framgia.music_29.data.source.SongDataSource;

public class SongLocalDataSoure {
    private static SongLocalDataSoure sInstance;

    public static SongLocalDataSoure getInstance() {
        if (sInstance == null) {
            sInstance = new SongLocalDataSoure();
        }
        return sInstance;
    }

    public void loadDataLocal(Context context, SongDataSource.LocalCallBack callBack) {
        new GetDataLocal(context , callBack).execute();
    }
}
