package com.framgia.music_29.screen.player;

import android.os.Environment;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.local.SqliteDownload;
import com.framgia.music_29.data.source.local.SqliteFavouriteSong;

public class PlayerPresenter implements PlayerContract.Presenter {
    private SqliteFavouriteSong mFavouriteSong;
    private PlayerContract.View mView;

    @Override
    public void setView(PlayerContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onGetFavoriteSong(SqliteFavouriteSong favouriteSong,String id) {
        mFavouriteSong = favouriteSong;
        return mFavouriteSong.getSongById(id);
    }

    public void onAddFavoriteSong(SqliteFavouriteSong favouriteSong, Song song) {
        mFavouriteSong = favouriteSong;
        mFavouriteSong.addSong(song);
    }

    public void onDeleteFavorite(SqliteFavouriteSong favouriteSong, Song song) {
        mFavouriteSong = favouriteSong;
        mFavouriteSong.deleteSong(song);
    }

    @Override
    public boolean onGetDownloadedSong(SqliteDownload sqlite, String id) {
        return sqlite.getSongById(id);
    }

    @Override
    public void onAddDownloadedSong(SqliteDownload sqlite, Song song) {
        sqlite.addSong(song);
    }
}
