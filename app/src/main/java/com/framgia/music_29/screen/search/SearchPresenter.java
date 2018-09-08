package com.framgia.music_29.screen.search;

import com.framgia.music_29.BuildConfig;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.source.SongDataSource;
import com.framgia.music_29.data.source.SongRepository;
import com.framgia.music_29.data.source.local.SongLocalDataSoure;
import com.framgia.music_29.data.source.remote.SongRemoteDataSource;
import com.framgia.music_29.utils.ConstantApi;

public class SearchPresenter implements SearchContract.Presenter, SongDataSource.CallBack {
    private SongRepository mSongRepository;
    private SearchContract.View mView;

    public SearchPresenter() {
        mSongRepository = SongRepository.getInstance(SongRemoteDataSource.getInstance(),
                SongLocalDataSoure.getInstance());
    }

    @Override
    public void setView(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadSongsSearch(String query) {
        StringBuilder url = new StringBuilder(ConstantApi.BASE_API_V1);
        url.append(ConstantApi.STRACK);
        url.append(query);
        url.append(ConstantApi.CLIENT_ID);
        url.append(BuildConfig.API_KEY);

        Genre genre = new Genre(url.toString());
        mSongRepository.getSongs(genre, this);
    }

    @Override
    public void onDataLoaded(Genre genre) {
        mView.setSongSuccess(genre.getSongs());
    }

    @Override
    public void onDataError(Exception e) {
        mView.loadSongsSearchError(e);
    }
}
