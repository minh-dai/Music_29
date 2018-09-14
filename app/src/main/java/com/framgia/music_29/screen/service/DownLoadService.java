package com.framgia.music_29.screen.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.utils.Constant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadService extends IntentService {
    private final String mRequest = "GET";
    private String link;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    private String fileName = "Music29";

    public DownLoadService() {
        super(String.valueOf(DownLoadService.class));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            link = intent.getStringExtra(getString(R.string.download));
            try {
                downLoadSong(link);
            } catch (IOException e) {
                onDownLoadError(e);
            }
        }
    }

    private void onDownLoadError(IOException e) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), getString(R.string.song_fail), Toast.LENGTH_SHORT)
                        .show();
            }
        };
        mainHandler.post(myRunnable);
    }

    private void downLoadSong(String link) throws IOException {
        InputStream inputStream = getInputStream(link);
        OutputStream fileOutputStream = new FileOutputStream(getOutPutFile());
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, length);
        }
        fileOutputStream.close();
        inputStream.close();
    }

    private InputStream getInputStream(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(mRequest);
        connection.setDoOutput(true);
        connection.connect();
        return connection.getInputStream();
    }

    private File getOutPutFile() throws IOException {
        StringBuilder path = new StringBuilder(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString());
        path.append(Constant.DOWNLOAD_DIRECTORY);

        File file = new File(path.toString() , fileName);
        if (!file.exists()) {
            file.mkdirs();
        }

        String fileName =
                URLUtil.guessFileName(link, null, MimeTypeMap.getFileExtensionFromUrl(link));

        File outputFile = new File(file, fileName);
        outputFile.createNewFile();

        return outputFile;
    }
}
