package com.framgia.music_29.screen.genre;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Genre;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.home.online.OnlineFragment;
import com.framgia.music_29.screen.player.PlayerActivity;
import com.framgia.music_29.screen.service.MusicService;
import com.framgia.music_29.utils.ConstantApi;
import com.framgia.music_29.utils.LoadMore;
import com.framgia.music_29.utils.PassSongService;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class GenreActivity extends AppCompatActivity
        implements GenreContract.View, View.OnClickListener, GenreAdapter.onClickItemListener,
        PassSongService {

    public static final String EXTRA_SONG = "EXTRA_SONG";
    public static final String EXTRA_LOCAL = "EXTRA_LOCAL";
    private GenreContract.Pesenter mPresenter;
    private GenreAdapter mAdapter;
    private TextView mTextGenre;
    private ImageView mImageBack;
    private RecyclerView mRecyclerGenre;
    private ProgressBar mProgressBar;
    private List<Song> mListSongs;
    private boolean mLocal;
    private Song mSong;
    private MusicService mService;
    private boolean mIsBound;
    private ImageView mImageSongGenre;
    private ImageView mImagePreGenre;
    private ImageView mImageNextGenre;
    private ImageView mImagePlayGenre;
    private TextView mTextSongGenre;
    private ConstraintLayout mLayoutGenre;
    private boolean mIsPlay;

    public static Intent getGenreIntent(Context context, Genre genre, Song song, boolean local,
            boolean isPlay) {
        Intent intent = new Intent(context, GenreActivity.class);
        intent.putExtra(OnlineFragment.EXTRA_GENRE_SONG, genre);
        intent.putExtra(GenreActivity.EXTRA_SONG, song);
        intent.putExtra(OnlineFragment.EXTRA_GENRE_LOCAL, local);
        intent.putExtra(OnlineFragment.EXTRA_GENRE_STATUS, isPlay);
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
        mImagePreGenre.setOnClickListener(this);
        mImageNextGenre.setOnClickListener(this);
        mImagePlayGenre.setOnClickListener(this);
    }

    private void initComponent() {
        mListSongs = new ArrayList<>();
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter = new GenrePresenter();
        mPresenter.setView(this);
        mAdapter = new GenreAdapter(this);
        mAdapter.setClickItemListener(this);
        mRecyclerGenre.setAdapter(mAdapter);
        mRecyclerGenre.setLayoutManager(new LinearLayoutManager(this));
        mLayoutGenre.setVisibility(View.GONE);
    }

    private void initView() {
        mImagePreGenre = findViewById(R.id.image_pre_genre);
        mImagePlayGenre = findViewById(R.id.image_play_genre);
        mImageNextGenre = findViewById(R.id.image_next_genre);
        mImageSongGenre = findViewById(R.id.image_song_genre);
        mTextSongGenre = findViewById(R.id.text_song_genre);
        mLayoutGenre = findViewById(R.id.constraint_genre);
        mTextGenre = findViewById(R.id.text_genre);
        mImageBack = findViewById(R.id.image_back);
        mRecyclerGenre = findViewById(R.id.recyclerview_genre);
        mProgressBar = findViewById(R.id.progress_loadding);
    }

    private void setAdapterSongs() {
        mSong = getIntent().getParcelableExtra(EXTRA_SONG);
        mIsPlay = getIntent().getBooleanExtra(OnlineFragment.EXTRA_GENRE_STATUS, false);
        mLocal = getIntent().getBooleanExtra(OnlineFragment.EXTRA_GENRE_LOCAL, false);
        Genre myGenre = getIntent().getParcelableExtra(OnlineFragment.EXTRA_GENRE_SONG);
        List<Song> songs = myGenre.getSongs();

        if (songs.size() > 0) {
            mListSongs = songs;
        } else {
            mRecyclerGenre.addOnScrollListener(
                    new LoadMore((LinearLayoutManager) mRecyclerGenre.getLayoutManager()) {
                        @Override
                        protected void OnLoadMoreItem() {
                            onLoadMore();
                        }
                    });
            mPresenter.loadDataForGenre(myGenre.getName());
        }
        setTitleActivity(myGenre.getName());
        passStatus(mIsPlay);
        passSong(mSong, mLocal);
    }

    @Override
    protected void onStop() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        mLayoutGenre.setVisibility(View.GONE);
        mPresenter.onStop();
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
                mTextGenre.setText(getString(R.string.playlist));
                mProgressBar.setVisibility(View.GONE);
                mAdapter.setSongs(mListSongs, true);
                break;
            case ConstantApi.GENRE_FAVOURITE:
                mProgressBar.setVisibility(View.GONE);
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
        mProgressBar.setVisibility(View.GONE);
        if(songs !=null){
            mListSongs.addAll(songs);
            mAdapter.setSongs(mListSongs);
        }
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
            case R.id.image_pre_genre:
                setImagePlay();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_PREVIOUS);
                break;
            case R.id.image_play_genre:
                setImagePauseSong();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_STATE);
                break;
            case R.id.image_next_genre:
                setImagePlay();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_NEXT);
                break;
            default:
                return;
        }
    }

    private void setImagePlay() {
        mImagePlayGenre.setEnabled(false);
        mIsPlay = false;
        setImagePauseSong();
    }

    private void onStartService(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        startService(intent);
    }

    @Override
    public void onClick(List<Song> songs, int position) {
        startService(MusicService.getInstance(this, songs, position, mLocal));
        startActivity(PlayerActivity.getPlayerIntent(this, songs.get(position), mLocal));
    }

    @Override
    public void onLoadMore() {
        mPresenter.onLoadMoreData();
    }

    @Override
    public void passSong(Song song, boolean local) {
        if (song != null) {
            if (local) {
                byte[] images = song.getUriImage();
                if (images == null) {
                    mImageSongGenre.setImageResource(R.drawable.item_music);
                } else {
                    mImageSongGenre.setImageBitmap(
                            BitmapFactory.decodeByteArray(images, 0, song.getUriImage().length));
                }
            } else {
                Picasso.with(this)
                        .load(song.getArtworkUrl())
                        .placeholder(R.drawable.item_music_app)
                        .into(mImageSongGenre);
            }
            mLayoutGenre.setVisibility(View.VISIBLE);
            mTextSongGenre.setText(song.getTitle());
            mLocal = local;
        } else {
            mLayoutGenre.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mService != null) {
            passSong(mService.getSong(), mService.isLocal());
            passStatus(!mService.isPlay());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mService != null) {
            passSong(mService.getSong(), mService.isLocal());
            passStatus(!mService.isPlay());
        }
    }

    @Override
    public void passStatus(boolean isPlay) {
        mImagePlayGenre.setEnabled(true);
        mIsPlay = isPlay;
        setImagePauseSong();
    }

    @Override
    public void setImagePauseSong() {
        if (!mIsPlay) {
            mImagePlayGenre.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mImagePlayGenre.setImageResource(android.R.drawable.ic_media_play);
        }
        mIsPlay = !mIsPlay;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mService == null) {
            Intent intentService = new Intent(this, MusicService.class);
            bindService(intentService, mConnection, Context.BIND_AUTO_CREATE);
        }
        mPresenter.onStart();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalService binder = (MusicService.LocalService) iBinder;
            mService = binder.getService();
            mService.setPassInterface(GenreActivity.this);
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
        }
    };
}
