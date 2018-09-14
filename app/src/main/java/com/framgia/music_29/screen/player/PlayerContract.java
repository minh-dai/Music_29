package com.framgia.music_29.screen.player;

import android.content.Context;
import com.framgia.music_29.BasePresenter;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.local.SqliteDownload;
import com.framgia.music_29.data.source.local.SqliteFavouriteSong;

public interface PlayerContract {

    interface Presenter extends BasePresenter<View>{
        boolean isExternalStorageReadable();

        boolean onGetFavoriteSong(SqliteFavouriteSong favouriteSong,String id);

        void onAddFavoriteSong(SqliteFavouriteSong favouriteSong,Song song);

        void onDeleteFavorite(SqliteFavouriteSong favouriteSong,Song song);

        boolean onGetDownloadedSong(SqliteDownload sqlite, String id);

        void onAddDownloadedSong(SqliteDownload sqlite, Song song);
    }

    interface View{
        void setImageShuffle();

        void setImagePauseSong();

        void setImageLoop();

        void setImageFavourite();
    }
}
