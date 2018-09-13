package com.framgia.music_29.data.source.remote;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.utils.Constant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadDataSource extends AsyncTask<String, Exception, String> {

    private final String mDone = "done";
    private final String mRequest = "GET";
    private final String mFileName = "MyMusic";
    private Context mContext;

    public DownloadDataSource(Context context) {
        mContext = context;
    }

    protected String doInBackground(String... params) {
        try {
            downLoadSong(params[0]);
            return mDone;
        } catch (IOException e) {
            publishProgress(e);
        }
        return "";
    }

    private void downLoadSong(String link) throws IOException {
        InputStream inputStream = getInputStream(link);
        FileOutputStream fileOutputStream = new FileOutputStream(getOutPutFile());
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
        String path = Environment.getExternalStorageDirectory() + Constant.DOWNLOAD_DIRECTORY;
        File file = new File(path);
        if (file.exists()) {
            file.mkdirs();
        }
        File outputFile = new File(file, mFileName);
        if (outputFile.exists()) {
            outputFile.createNewFile();
        }
        return outputFile;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equals(mDone)) {
            Toast.makeText(mContext, mContext.getString(R.string.download), Toast.LENGTH_SHORT)
                    .show();
        }else {
            Toast.makeText(mContext, mContext.getString(R.string.song_download), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onProgressUpdate(Exception... values) {
        super.onProgressUpdate(values);
        Toast.makeText(mContext, values[0].getMessage(), Toast.LENGTH_SHORT).show();
    }
}