package com.framgia.music_29.screen.home.offline;

import android.content.ContentResolver;
import android.content.Context;
import com.framgia.music_29.BasePresenter;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.local.SqliteFavouriteSong;
import java.util.List;

public interface OfflineFragmentContract {
    interface Presenter extends BasePresenter<View> {
        void loadDataPlaylist(ContentResolver contentResolver);

        void loadDataFavorite(SqliteFavouriteSong mFavouriteSong);

        void loadDataAlbum();

        void loadSongDownload();
    }

    interface View {
        void onFavoriteClicked(List<Song> songs);

        void onPlayListClicked(List<Song> songs);

        void onDownloadClicked(List<Song> songs);

        void onAlbumClicked(List<Song> songs);

        void onDataError();
    }
}
