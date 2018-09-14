package com.framgia.music_29.screen.home.offline;

import android.content.ContentResolver;
import android.content.Context;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.SongDataSource;
import com.framgia.music_29.data.source.SongRepository;
import com.framgia.music_29.data.source.local.SongLocalDataSoure;
import com.framgia.music_29.data.source.local.SqliteFavouriteSong;
import com.framgia.music_29.data.source.remote.SongRemoteDataSource;
import java.util.List;

public class OfflineFragmentPresenter
        implements OfflineFragmentContract.Presenter, SongDataSource.LocalCallBack {

    private OfflineFragmentContract.View mView;
    private SongRepository mSongRepository;

    public OfflineFragmentPresenter() {
        mSongRepository = SongRepository.getInstance(SongRemoteDataSource.getInstance(),
                SongLocalDataSoure.getInstance());
    }

    @Override
    public void setView(OfflineFragmentContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void loadDataPlaylist(ContentResolver contentResolver) {
        mSongRepository.getDataLocal(contentResolver ,this);
    }

    @Override
    public void loadDataFavorite(SqliteFavouriteSong mFavouriteSong) {
        mView.onFavoriteClicked(setIsStream(mFavouriteSong.getAllSong()));
    }

    private List<Song> setIsStream(List<Song> allStudent) {
        for (int i=0; i<allStudent.size() ; ++i){
            allStudent.get(i).setStreamable(true);
        }
        return allStudent;
    }

    @Override
    public void loadDataAlbum() {

    }

    @Override
    public void loadSongDownload() {

    }

    @Override
    public void onDataLoaded(List<Song> songs) {
        mView.onPlayListClicked(songs);
    }

    @Override
    public void onDataError() {
        mView.onDataError();
    }
}
