package com.framgia.music_29.data.source;

import android.content.ContentResolver;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.source.local.SongLocalDataSoure;
import com.framgia.music_29.data.source.remote.SongRemoteDataSource;

public class SongRepository implements SongDataSource {
    private static SongRepository sSongRepository;
    private SongRemoteDataSource mSongRemoteDataSource;
    private SongLocalDataSoure mSongLocalDataSoure;

    public SongRepository(SongRemoteDataSource songRemoteDataSource,
            SongLocalDataSoure songLocalDataSoure) {
        mSongRemoteDataSource = songRemoteDataSource;
        mSongLocalDataSoure = songLocalDataSoure;
    }

    public static SongRepository getInstance(SongRemoteDataSource remoteDataSource, SongLocalDataSoure songLocalDataSoure) {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(remoteDataSource, songLocalDataSoure);
        }
        return sSongRepository;
    }

    @Override
    public void getDatas(Genre genre, CallBack callBack) {
        mSongRemoteDataSource.loadData(genre, callBack);
    }

    @Override
    public void getSongs(Genre genre, CallBack callBack) {
        mSongRemoteDataSource.loadSong(genre, callBack);
    }

    public void getDataLocal(ContentResolver contentResolver, LocalCallBack callback) {
        mSongLocalDataSoure.loadDataLocal(contentResolver, callback);
    }
}
