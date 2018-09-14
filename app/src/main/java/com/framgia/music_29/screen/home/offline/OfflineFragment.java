package com.framgia.music_29.screen.home.offline;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.local.SqliteFavouriteSong;
import com.framgia.music_29.screen.genre.GenreActivity;
import com.framgia.music_29.screen.home.HomeActivity;
import java.util.List;

public class OfflineFragment extends Fragment
        implements OfflineFragmentContract.View, View.OnClickListener,
        HomeActivity.IPassSongOffline {

    private OfflineFragmentContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private SqliteFavouriteSong mFavouriteSong;
    private Song mSong;
    private HomeActivity mActivity;
    private boolean mLocal;
    private boolean mIsPlay;

    public static OfflineFragment newInstance() {
        return new OfflineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offline, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerEventListener(view);
        initComponent();
    }

    private void initComponent() {
        mPresenter = new OfflineFragmentPresenter();
        mPresenter.setView(this);
        mFavouriteSong = new SqliteFavouriteSong(getContext());

        mActivity = (HomeActivity) getActivity();
        mActivity.setIPassSongOffline(this);
    }

    private void registerEventListener(View view) {
        mProgressBar = view.findViewById(R.id.progress_bar);
        view.findViewById(R.id.layout_favotite).setOnClickListener(this);
        view.findViewById(R.id.layout_song).setOnClickListener(this);
        view.findViewById(R.id.layout_playlist).setOnClickListener(this);
        view.findViewById(R.id.layout_album).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isExternalStorageReadable()) {
            switch (v.getId()) {
                case R.id.layout_favotite:
                    mLocal = false;
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPresenter.loadDataFavorite(mFavouriteSong);
                    break;
                case R.id.layout_playlist:
                    mLocal = true;
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPresenter.loadDataPlaylist(getActivity().getContentResolver());
                    break;
                case R.id.layout_song:
                    mPresenter.loadSongDownload();
                    break;
                case R.id.layout_album:
                    mPresenter.loadDataAlbum();
                    break;
            }
        }else {
            Toast.makeText(getActivity() , getString(R.string.not_find_data) , Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onFavoriteClicked(List<Song> songs) {
        onStartGenreAvtivity(songs, getString(R.string.favorite_songs), false);
    }

    @Override
    public void onPlayListClicked(List<Song> songs) {
        onStartGenreAvtivity(songs, getString(R.string.playlist), true);
    }

    @Override
    public void onDownloadClicked(List<Song> songs) {

    }

    @Override
    public void onAlbumClicked(List<Song> songs) {

    }

    @Override
    public void onDataError() {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(getContext() ,getString(R.string.string_null) , Toast.LENGTH_SHORT).show();
    }

    private void onStartGenreAvtivity(List<Song> songs, String genre, boolean local) {
        mProgressBar.setVisibility(View.GONE);
        if (songs.size() > 0) {
            Genre genre1 = new Genre();
            genre1.setName(genre);
            genre1.setSongs(songs);
            startActivity(GenreActivity.getGenreIntent(getContext(), genre1, mSong, local , mIsPlay));
        }else {
            Toast.makeText(getContext() ,getString(R.string.string_null) , Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void passSong(Song song) {
        mSong = song;
    }

    @Override
    public void passStatus(boolean isPLay) {
        mIsPlay = isPLay;
    }
}
