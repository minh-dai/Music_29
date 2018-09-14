package com.framgia.music_29.utils;

import android.support.annotation.StringDef;
import com.framgia.music_29.BuildConfig;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface ConstantApi {
    String BASE_API = "https://api-v2.soundcloud.com";
    String BASE_API_V1 = "https://api.soundcloud.com";
    String CLIENT_ID = "&client_id=";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ PARA_MUSIC_GENRE, GENRE_ALL_MUSIC, GENRE_ALL_AUDIO })
    @interface Constant {
    }

    String GENRE_ALL_MUSIC = "all-music";
    String GENRE_ALL_AUDIO = "all-audio";
    String PARA_MUSIC_GENRE = "/charts?kind=top&genre=soundcloud:genres:";
    String LIMIT = "&limit=";
    String OFFSET = "&offset=";
    String STREAM = "/stream?";
    String CHARTS = "/charts?";
    String STRACK = "/tracks?q=";
    String KIND = "kind=";
    String GENRE = " &genre=";
    String AGENRES = "%3Agenres%3";
    String QUERY_URN = "&query_urn";
    String ACHARTS = "%3Acharts%3";
    String GENRE_ALTERNATIVEROCK = "alternativerock";
    String GENRE_AMBIENT = "ambient";
    String GENRE_CLASSICAL = "classical";
    String GENRE_COUNTRY = "country";
    String GENRE_PLAYLIST = "PlayList";
    String GENRE_FAVOURITE = "Favourite Songs";
    String PLAY_URL= STREAM+CLIENT_ID+ BuildConfig.API_KEY;
    String CLIENT_DOWNLOAD = "?client_id=";
    String LINK_DOWNLOAD = CLIENT_DOWNLOAD + BuildConfig.API_KEY;

}
