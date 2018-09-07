package com.framgia.music_29.screen.home.offline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.music_29.R;

public class OfflineFragment extends Fragment
        implements OfflineFragmentContract.View, View.OnClickListener {

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
                onFavoriteClicked();
                break;
            case R.id.layout_playlist:
                onPlayListClicked();
                break;
            case R.id.layout_song:
                onSongClicked();
                break;
            case R.id.layout_album:
                onAlbumClicked();
                break;
        }
    }

    @Override
    public void onFavoriteClicked() {

    }

    @Override
    public void onPlayListClicked() {

    }

    @Override
    public void onSongClicked() {

    }

    @Override
    public void onAlbumClicked() {

    }
}
