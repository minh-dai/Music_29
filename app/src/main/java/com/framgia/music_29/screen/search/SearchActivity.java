package com.framgia.music_29.screen.search;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.home.online.OnlineFragment;
import com.framgia.music_29.screen.player.PlayerActivity;
import com.framgia.music_29.screen.service.MusicService;
import com.framgia.music_29.utils.OnClickItemListener;
import com.framgia.music_29.utils.PassSongService;
import com.squareup.picasso.Picasso;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements SearchContract.View, View.OnClickListener, OnClickItemListener,PassSongService {

    private RecyclerView mRecyclerView;
    private ImageView mImageBack;
    private ImageView mImageSearch;
    private ImageView mImageFail;
    private EditText mEditSearch;
    private ProgressBar mProgressBar;
    private SearchContract.Presenter mPresenter;
    private SearchAdapter mAdapter;
    private ConstraintLayout mLayoutSearch;
    private TextView mTextSongSearch;
    private ImageView mImageSongSearch;
    private ImageView mImagePlaySearch;
    private ImageView mImagePreSearch;
    private ImageView mImageNextSearch;
    private Song mSong;
    private MusicService mService;
    private boolean mLocal;
    private boolean mIsPlay;
    private boolean mIsBound;

    public static Intent getSearchIntent(Context context, Song song, boolean isPlay, boolean local) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(OnlineFragment.EXTRA_GENRE_SONG, song);
        intent.putExtra(OnlineFragment.EXTRA_GENRE_STATUS, isPlay);
        intent.putExtra(OnlineFragment.EXTRA_GENRE_LOCAL, local);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        initComponents();
        registerEventListeners();
    }

    private void registerEventListeners() {
        mImageBack.setOnClickListener(this);
        mImageSearch.setOnClickListener(this);
        mImagePlaySearch.setOnClickListener(this);
        mImagePreSearch.setOnClickListener(this);
        mImageNextSearch.setOnClickListener(this);
    }

    private void initComponents() {
        mPresenter = new SearchPresenter();
        mPresenter.setView(this);
        mAdapter = new SearchAdapter(this);
        mAdapter.setClickItemListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mSong = getIntent().getParcelableExtra(OnlineFragment.EXTRA_GENRE_SONG);
        mIsPlay = !getIntent().getBooleanExtra(OnlineFragment.EXTRA_GENRE_STATUS, false);
        mLocal = getIntent().getBooleanExtra(OnlineFragment.EXTRA_GENRE_LOCAL, false);
        passSong(mSong, mLocal);
        passStatus(mIsPlay);
    }

    private void initViews() {
        mImageSongSearch = findViewById(R.id.image_song_search);
        mLayoutSearch = findViewById(R.id.constraint_search);
        mImagePlaySearch = findViewById(R.id.image_play_search);
        mImagePreSearch = findViewById(R.id.image_pre_search);
        mImageNextSearch = findViewById(R.id.image_next_search);
        mTextSongSearch = findViewById(R.id.text_song_search);

        mRecyclerView = findViewById(R.id.recylerview_search);
        mImageBack = findViewById(R.id.image_back_search);
        mImageSearch = findViewById(R.id.image_search);
        mImageFail = findViewById(R.id.image_fail);
        mEditSearch = findViewById(R.id.edit_search);
        mProgressBar = findViewById(R.id.progressbar_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back_search:
                onBackPressed();
                finish();
                break;
            case R.id.image_search:
                searchSong();
                break;
            case R.id.image_pre_search:
                setImagePlay();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_PREVIOUS);
                break;
            case R.id.image_play_search:
                setImagePauseSong();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_STATE);
                break;
            case R.id.image_next_search:
                setImagePlay();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_NEXT);
                break;
            default:
                break;
        }
    }

    private void setImagePlay(){
        mImagePlaySearch.setEnabled(false);
        mIsPlay = false;
        setImagePauseSong();
    }

    private void onStartService(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        startService(intent);
    }

    private void searchSong() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        mImageFail.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.loadSongsSearch(mEditSearch.getText().toString());
    }

    @Override
    public void setSongSuccess(List<Song> songs) {
        if (songs.size() > 0) {
            mImageFail.setVisibility(View.GONE);
            mAdapter.setSongs(songs);
        } else {
            mImageFail.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.not_find_data), Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadSongsSearchError(Exception e) {
        mImageFail.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mService != null) {
            mLocal = mService.isLocal();
            passSong(mService.getSong(), mLocal);
            passStatus(!mService.isPlay());
        }
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

    @Override
    protected void onStop() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
        mLayoutSearch.setVisibility(View.GONE);
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onClick(List<Song> songs, int position) {
        startActivity(PlayerActivity.getPlayerIntent(this, songs.get(position) , false));
        startService(MusicService.getInstance(this, songs, position, false));
    }

    @Override
    public void passSong(Song song, boolean local) {
        if (song != null) {
           if (local) {
               byte[] images = song.getUriImage();
               if (images == null) {
                   mImageSongSearch.setImageResource(R.drawable.item_music);
               } else {
                   mImageSongSearch.setImageBitmap(BitmapFactory.decodeByteArray(images, 0, song.getUriImage().length));
               }
           }else {
               Picasso.with(this)
                       .load(song.getArtworkUrl())
                       .placeholder(R.drawable.item_music_app)
                       .into(mImageSongSearch);
           }
            mLayoutSearch.setVisibility(View.VISIBLE);
            mTextSongSearch.setText(song.getTitle());
        } else {
            mLayoutSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void passStatus(boolean isPlay) {
        mImagePlaySearch.setEnabled(true);
        mIsPlay = isPlay;
        setImagePauseSong();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalService binder = (MusicService.LocalService) iBinder;
            mService = binder.getService();
            mService.setPassInterface(SearchActivity.this);
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
        }
    };

    @Override
    public void setImagePauseSong() {
        if (!mIsPlay) {
            mImagePlaySearch.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mImagePlaySearch.setImageResource(android.R.drawable.ic_media_play);
        }
        mIsPlay = !mIsPlay;
    }
}
