package com.framgia.music_29.screen.genre;

import com.framgia.music_29.BuildConfig;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.source.SongDataSource;
import com.framgia.music_29.data.source.SongRepository;
import com.framgia.music_29.data.source.remote.SongRemoteDataSource;
import com.framgia.music_29.utils.ConstantApi;

public class GenrePresenter implements GenreContract.Pesenter, SongDataSource.CallBack {

    private GenreContract.View mView;
    private SongRepository mSongRepository;
    private Genre mGenre;

    public GenrePresenter() {
        mSongRepository = SongRepository.getInstance(SongRemoteDataSource.getInstance());
        mGenre = new Genre();
    }

    @Override
    public void setView(GenreContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadDataForGenre(String genre_song) {
        StringBuilder url =  new StringBuilder(ConstantApi.BASE_API);
        url.append(ConstantApi.PARA_MUSIC_GENRE);
        url.append(genre_song);
        url.append(ConstantApi.CLIENT_ID);
        url.append(BuildConfig.API_KEY);

        Genre genre = new Genre(url.toString());
        mSongRepository.getDatas(genre, this);
    }

    @Override
    public void onLoadMoreData() {
        mSongRepository.getDatas(mGenre, this);
    }

    @Override
    public void onDataLoaded(Genre genre) {
        StringBuilder url =  new StringBuilder(genre.getUrl());
        url.append(ConstantApi.CLIENT_ID);
        url.append(BuildConfig.API_KEY);
        mGenre.setUrl(url.toString());
        mView.onSetSongSuccess(genre.getSongs());
    }

    @Override
    public void onDataError(Exception e) {
        mView.onDataNotAvailable(e);
    }
}
