package com.framgia.music_29.screen.genre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.home.offline.OfflineFragment;
import com.framgia.music_29.screen.home.online.OnlineFragment;
import com.framgia.music_29.screen.player.PlayerActivity;
import com.framgia.music_29.screen.service.MusicService;
import com.framgia.music_29.utils.ConstantApi;
import com.framgia.music_29.utils.LoadMore;
import java.util.ArrayList;
import java.util.List;

public class GenreActivity extends AppCompatActivity
        implements GenreContract.View, View.OnClickListener, GenreAdapter.onClickItemListener {

    public static final String EXTRA_SONG = "song";
    private GenreContract.Pesenter mPesenter;
    private GenreAdapter mAdapter;
    private TextView mTextGenre;
    private ImageView mImageBack;
    private RecyclerView mRecyclerGenre;
    private ProgressBar mProgressBar;
    private List<Song> mListSongs;
    private static boolean mLocal;

    public static Intent getGenreIntent(Context context, String genre) {
        Intent intent = new Intent(context, GenreActivity.class);
        intent.putExtra(OnlineFragment.EXTRA_GENRE, genre);
        return intent;
    }

    public static Intent getGenreIntent(Context context, String genre, List<Song> songs) {
        Intent intent = new Intent(context, GenreActivity.class);
        intent.putExtra(OnlineFragment.EXTRA_GENRE, genre);
        intent.putParcelableArrayListExtra(OfflineFragment.EXTRA_LIST_GENRE,
                (ArrayList<? extends Parcelable>) songs);
        mLocal = true;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        initView();
        initComponent();
        onCLickListenEvent();
        setAdapterSongs();
    }

    private void onCLickListenEvent() {
        mImageBack.setOnClickListener(this);
    }

    private void initComponent() {
        mListSongs = new ArrayList<>();
        mProgressBar.setVisibility(View.VISIBLE);
        mPesenter = new GenrePresenter();
        mPesenter.setView(this);
        mAdapter = new GenreAdapter(this);
        mAdapter.setClickItemListener(this);
        mRecyclerGenre.setAdapter(mAdapter);
        mRecyclerGenre.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        mTextGenre = findViewById(R.id.text_genre);
        mImageBack = findViewById(R.id.image_back);
        mRecyclerGenre = findViewById(R.id.recyclerview_genre);
        mProgressBar = findViewById(R.id.progress_loadding);
    }

    private void setAdapterSongs() {
        String genre = getIntent().getStringExtra(OnlineFragment.EXTRA_GENRE);
        List<Song> songs =
                getIntent().getParcelableArrayListExtra(OfflineFragment.EXTRA_LIST_GENRE);
        if (!mLocal) {
            mRecyclerGenre.addOnScrollListener(
                    new LoadMore((LinearLayoutManager) mRecyclerGenre.getLayoutManager()) {
                        @Override
                        protected void OnLoadMoreItem() {
                            onLoadMore();
                        }
                    });
            mPesenter.loadDataForGenre(genre);
        } else {
            mListSongs = songs;
        }
        setTitleActivity(genre);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPesenter.onStart();
    }

    @Override
    protected void onStop() {
        mPesenter.onStop();
        super.onStop();
    }

    private void setTitleActivity(String title) {
        switch (title) {
            case ConstantApi.GENRE_ALL_MUSIC:
                mTextGenre.setText(getString(R.string.genre_all_music));
                break;
            case ConstantApi.GENRE_ALL_AUDIO:
                mTextGenre.setText(getString(R.string.genre_all_audio));
                break;
            case ConstantApi.GENRE_ALTERNATIVEROCK:
                mTextGenre.setText(getString(R.string.genre_alternativerock));
                break;
            case ConstantApi.GENRE_AMBIENT:
                mTextGenre.setText(getString(R.string.genre_ambient));
                break;
            case ConstantApi.GENRE_CLASSICAL:
                mTextGenre.setText(getString(R.string.genre_classical));
                break;
            case ConstantApi.GENRE_COUNTRY:
                mTextGenre.setText(getString(R.string.genre_country));
                break;
            case ConstantApi.GENRE_PLAYLIST:
                mLocal = true;
                mTextGenre.setText(getString(R.string.playlist));
                mAdapter.setSongs(mListSongs, true);
                break;
            case ConstantApi.GENRE_FAVOURITE:
                mLocal = false;
                mAdapter.setSongs(mListSongs);
                mTextGenre.setText(getString(R.string.favorite_songs));
                break;
            default:
                mTextGenre.setText("");
                break;
        }
    }

    @Override
    public void onSetSongSuccess(List<Song> songs) {
        mListSongs.addAll(songs);
        mAdapter.setSongs(mListSongs);
    }

    @Override
    public void onDataNotAvailable(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back:
                onBackPressed();
                finish();
                break;
            default:
                return;
        }
    }

    @Override
    public void onClick(List<Song> songs, int position) {
        if (mLocal) {
            startActivity(PlayerActivity.getPlayerIntent(this, songs.get(position), mLocal));
            startService(MusicService.getInstance(this, mLocal, songs, position));
        } else {
            startActivity(PlayerActivity.getPlayerIntent(this, songs.get(position)));
            startService(MusicService.getInstance(this, songs, position));
        }
    }

    @Override
    public void onLoadMore() {
        mPesenter.onLoadMoreData();
    }
}
