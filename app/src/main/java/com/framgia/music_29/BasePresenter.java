package com.framgia.music_29;

public interface BasePresenter <T> {
    void setView(T view);

    void onStart();

    void onStop();
}
