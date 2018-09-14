package com.framgia.music_29.screen.service;

import com.framgia.music_29.data.model.Song;

public interface OnMusicListener {

    void onPlayMedia(Song song);

    void onPauseMedia();

    void onPreviousMedia();

    void onNextMedia();

    void onStopService();

    void onLoopMedia();

    void onRandomMedia();

    void setSeekTo(int seekBarProgress);

    boolean isPlay();

    Song getSong();
}
