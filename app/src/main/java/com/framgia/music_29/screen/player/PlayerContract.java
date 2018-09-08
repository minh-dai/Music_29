package com.framgia.music_29.screen.player;

import com.framgia.music_29.BasePresenter;

public interface PlayerContract {

    interface Presenter extends BasePresenter<View>{

    }

    interface View{
        void setImageShuffle();

        void setImagePauseSong();

        void setImageLoop();
    }
}
