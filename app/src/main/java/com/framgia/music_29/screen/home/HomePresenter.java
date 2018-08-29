package com.framgia.music_29.screen.home;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View mView;

    @Override
    public void setView(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }
}
