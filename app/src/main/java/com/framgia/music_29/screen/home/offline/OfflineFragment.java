package com.framgia.music_29.screen.home.offline;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.genre.GenreActivity;
import java.util.List;

public class OfflineFragment extends Fragment
        implements OfflineFragmentContract.View, View.OnClickListener {

    public static String EXTRA_LIST_GENRE = "com.framgia.music_29.EXTRA_LIST_GENRE";
    private OfflineFragmentContract.Presenter mPresenter;

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
        initComponent();
        if (isExternalStorageReadable()) {
            registerEventListener(view);
        }
    }

    private void initComponent() {
        mPresenter = new OfflineFragmentPresenter();
        mPresenter.setView(this);
    }

    private void registerEventListener(View view) {
        view.findViewById(R.id.layout_favotite).setOnClickListener(this);
        view.findViewById(R.id.layout_playlist).setOnClickListener(this);
        view.findViewById(R.id.layout_song).setOnClickListener(this);
        view.findViewById(R.id.layout_album).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_favotite:
                mPresenter.loadDataFavorite();
                break;
            case R.id.layout_playlist:
                mPresenter.loadDataPlaylist(getContext().getContentResolver());
                break;
            case R.id.layout_song:
                mPresenter.loadDataDownload();
                break;
            case R.id.layout_album:
                mPresenter.loadDataAlbum();
                break;
        }
    }

    @Override
    public void onFavoriteClicked(List<Song> songs) {

    }

    @Override
    public void onPlayListClicked(List<Song> songs) {
        onStartGenreAvtivity(songs, getString(R.string.playlist));
    }

    @Override
    public void onDownloadClicked(List<Song> songs) {

    }

    @Override
    public void onAlbumClicked(List<Song> songs) {

    }

    @Override
    public void onDataError() {
        Toast.makeText(getContext(), getString(R.string.string_null), Toast.LENGTH_SHORT).show();
    }

    private void onStartGenreAvtivity(List<Song> songs, String genre) {
        startActivity(GenreActivity.getGenreIntent(getContext(), genre, songs));
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
