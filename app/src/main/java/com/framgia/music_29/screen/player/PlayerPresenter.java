package com.framgia.music_29.screen.player;

import android.os.Environment;

public class PlayerPresenter implements PlayerContract.Presenter {
    @Override
    public void setView(PlayerContract.View view) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
