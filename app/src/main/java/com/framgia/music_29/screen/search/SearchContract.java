package com.framgia.music_29.screen.search;

import com.framgia.music_29.BasePresenter;
import com.framgia.music_29.data.model.Song;
import java.util.List;

public interface SearchContract {
    interface Presenter extends BasePresenter<View> {
        void loadSongsSearch(String query);
    }

    interface View {
        void setSongSuccess(List<Song> songs);

        void loadSongsSearchError(Exception e);
    }
}
