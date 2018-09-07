package com.framgia.music_29.screen.home.offline;

import com.framgia.music_29.BasePresenter;

public interface OfflineFragmentContract {
    interface Presenter extends BasePresenter<View>{
    }

    interface View {
        void onFavoriteClicked();

        void onPlayListClicked();

        void onSongClicked();

        void onAlbumClicked();
    }
}
