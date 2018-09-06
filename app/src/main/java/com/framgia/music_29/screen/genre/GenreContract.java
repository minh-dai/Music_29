package com.framgia.music_29.screen.genre;

import android.content.Context;
import com.framgia.music_29.BasePresenter;
import com.framgia.music_29.data.model.Song;
import java.util.List;

public interface GenreContract {
    interface Pesenter extends BasePresenter<View>{
        void loadDataForGenre(String genre_song);

        void onLoadMoreData();

    }

    interface View{

        void onSetSongSuccess(List<Song> songs);

        void onDataNotAvailable(Exception e);
    }
}
