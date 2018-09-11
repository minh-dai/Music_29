package com.framgia.music_29.data.source.remote;

import android.os.AsyncTask;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.SongDataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetDataApi extends AsyncTask<String, Exception, Genre> {

    private final String mConllection = "collection";
    private final String mTrack = "track";
    private final String mRequest = "GET";
    private final int mTimeOutConnect = 3000;
    private final String mNextHref = "next_href";
    private SongDataSource.CallBack mCallBack;
    private Genre mGenre;

    public GetDataApi(Genre genre, SongDataSource.CallBack callBack) {
        mGenre = genre;
        mCallBack = callBack;
    }

    @Override
    protected Genre doInBackground(String... strings) {
        List<Song> song = new ArrayList<>();
        try {
            song = convertSong(getDataFromUrl(strings[0]));
        } catch (JSONException e) {
            publishProgress(e);
        } catch (IOException e) {
            publishProgress(e);
        }
        mGenre.setSongs(song);
        return mGenre;
    }

    @Override
    protected void onPostExecute(Genre genre) {
        super.onPostExecute(genre);
        mCallBack.onDataLoaded(genre);
    }

    @Override
    protected void onProgressUpdate(Exception... values) {
        super.onProgressUpdate(values);
        mCallBack.onDataError(values[0]);
    }

    private List<Song> convertSong(String data) throws JSONException {
        List<Song> mList = new ArrayList<>();
        JSONObject object = new JSONObject(data);
        mGenre.setUrl(object.getString(mNextHref));
        JSONArray array = object.getJSONArray(mConllection);
        for (int i = 0; i < array.length(); ++i) {
            JSONObject track = array.getJSONObject(i).getJSONObject(mTrack);
            mList.add(parseJson(track));
        }
        return mList;
    }

    private Song parseJson(JSONObject track) throws JSONException {
        Song song = new Song(track);
        return song;
    }

    private String getDataFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpsURLConnection mConnection = (HttpsURLConnection) url.openConnection();
        mConnection.setRequestMethod(mRequest);
        mConnection.setConnectTimeout(mTimeOutConnect);
        mConnection.setReadTimeout(mTimeOutConnect);
        mConnection.connect();

        InputStream inputStream = mConnection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
