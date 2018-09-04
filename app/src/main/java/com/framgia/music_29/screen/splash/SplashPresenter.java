package com.framgia.music_29.screen.splash;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;

    public SplashPresenter() {
    }

    @Override
    public void setView(SplashContract.View view) {
        mView = view;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {

    }

}
