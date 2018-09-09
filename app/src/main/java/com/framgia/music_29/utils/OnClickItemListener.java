package com.framgia.music_29.utils;

import com.framgia.music_29.data.model.Song;
import java.util.List;

public interface OnClickItemListener {
    void onClick(List<Song> songs, int position);
}
