package com.framgia.music_29.data.source.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.SongDataSource;
import java.util.ArrayList;
import java.util.List;

public class GetDataLocal extends AsyncTask<Void, List<Song>, List<Song>> {

    private Context mContext;
    private SongDataSource.LocalCallBack mLoadSongsCallback;

    public GetDataLocal(Context context, SongDataSource.LocalCallBack loadSongsCallback) {
        mContext = context;
        mLoadSongsCallback = loadSongsCallback;
    }

    @Override
    protected List<Song> doInBackground(Void... voids) {
        List<Song> songs = new ArrayList<>();
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(baseUri, null, null, null, null);

        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        } else {
            parseSong(cursor);
            while (cursor.moveToNext()) {
                songs.add(parseSong(cursor));
            }
        }
        return songs;
    }

    @Override
    protected void onPostExecute(List<Song> songs) {
        super.onPostExecute(songs);
        if (songs == null || songs.isEmpty()) {
            mLoadSongsCallback.onDataError();
        } else {
            mLoadSongsCallback.onDataLoaded(songs);
        }
    }

    private Song parseSong(Cursor cursor) {
        return new Song(cursor);
    }
}
