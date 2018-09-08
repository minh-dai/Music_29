package com.framgia.music_29.screen.player;

import com.framgia.music_29.data.model.Song;

public interface IUpdateUi {
    void setTextTime(int current, int endTime);

    void setImagePlay(String status);

    void setSong(Song song);

}
