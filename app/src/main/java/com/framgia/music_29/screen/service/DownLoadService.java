package com.framgia.music_29.screen.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import com.framgia.music_29.data.source.remote.DownloadDataSource;

public class DownLoadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownLoadService(String name) {
        super("DownLoadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       // new DownloadDataSource(context).execute(url);
    }
}
