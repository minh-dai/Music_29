package com.framgia.music_29.screen.splash;

import com.framgia.music_29.BuildConfig;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.source.SongDataSource;
import com.framgia.music_29.data.source.SongRepository;
import com.framgia.music_29.data.source.remote.SongRemoteDataSource;
import com.framgia.music_29.utils.ConstantApi;

public class SplashPresenter implements SplashContract.Presenter, SongDataSource.CallBack {

    private SplashContract.View mView;
    private SongRepository mSongRepository;

    public SplashPresenter() {
        mSongRepository = SongRepository.getInstance(SongRemoteDataSource.getInstance());
    }

    @Override
    public void setView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void loadDataMusics(String genresAllMusic) {
        String urlAllMusic = ConstantApi.BASE_API
                + ConstantApi.PARA_MUSIC_GENRE
                + genresAllMusic
                + ConstantApi.CLIENT_ID
                + BuildConfig.API_KEY;

        Genre genre = new Genre(genresAllMusic, urlAllMusic);
        mSongRepository.getDatas(genre, this);
    }

    @Override
    public void loadDataAudios(String genresAllAudio) {
        String urlAllAudio = ConstantApi.BASE_API
                + ConstantApi.PARA_MUSIC_GENRE
                + genresAllAudio
                + ConstantApi.CLIENT_ID
                + BuildConfig.API_KEY;

        Genre genre = new Genre(genresAllAudio, urlAllAudio);
        mSongRepository.getDatas(genre, this);
    }

    @Override
    public void onDataLoaded(Genre genre) {
        if (genre.getName().equals(ConstantApi.GENRE_ALL_MUSIC)) {
            mView.setDataMusics(genre.getList());
        } else {
            mView.setDataAudios(genre.getList());
        }
        mView.checkList();
    }

    @Override
    public void onDataError(Exception e) {
        mView.onDataError(e);
    }
}
