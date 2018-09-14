package com.framgia.music_29.utils;

import com.framgia.music_29.data.model.Song;

public interface PassSongService {

    void passSong(Song song, boolean local);

    void passStatus(boolean isPlay);

    void setImagePauseSong();
}
