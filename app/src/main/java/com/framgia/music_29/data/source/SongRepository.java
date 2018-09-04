package com.framgia.music_29.data.source;

import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.source.remote.SongRemoteDataSource;

public class SongRepository implements SongDataSource {
    private static SongRepository sSongRepository;
    private SongRemoteDataSource mSongRemoteDataSource;

    public SongRepository(SongRemoteDataSource songRemoteDataSource) {
        mSongRemoteDataSource = songRemoteDataSource;
    }

    public static SongRepository getInstance(SongRemoteDataSource remoteDataSource) {
        if (sSongRepository == null) {
            sSongRepository = new SongRepository(remoteDataSource);
        }
        return sSongRepository;
    }

    @Override
    public void getDatas(Genre genre, CallBack callBack) {
        mSongRemoteDataSource.loadData(genre, callBack);
    }
}
