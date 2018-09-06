package com.framgia.music_29.screen.splash;

import com.framgia.music_29.BasePresenter;
import com.framgia.music_29.data.model.Song;
import java.util.List;

public interface SplashContract {

    interface Presenter extends BasePresenter<View> {

        void loadDataMusics(String genresAllMusic);

        void loadDataAudios(String genresAllAudio);
    }

    interface View {
        void setDataMusics(List<Song> list);

        void setDataAudios(List<Song> list);

        void checkList();

        void onDataError(Exception e);
    }
}
