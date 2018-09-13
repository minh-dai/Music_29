package com.framgia.music_29.screen.player;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.genre.GenreActivity;
import com.framgia.music_29.screen.service.MusicService;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PlayerActivity extends AppCompatActivity
        implements PlayerContract.View, View.OnClickListener, IUpdateUi {

    public static final String ACTION_MEDIA_PAUSE = "pause";
    public static final String ACTION_MEDIA_START = "start";
    public static final String ACTION_MEDIA_FAIL = "fail";
    private PlayerContract.Presenter mPresenter;
    private ImageView mImageFavorite;
    private ImageView mImageDownload;
    private ImageView mImageLoop;
    private ImageView mImageShuffle;
    private ImageView mImageSong;
    private ImageButton mButtonPre;
    private ImageButton mButtonPlay;
    private ImageButton mButtonNext;
    private ImageView mButtonBack;
    private TextView mTextSongName;
    private TextView mTextCurrentTime;
    private TextView mTextEndTime;
    private SeekBar mSeekBarSong;
    private MusicService mService;
    private Song mSong;
    private boolean mIsBound = false;
    private boolean mIsPlay;
    private boolean mIsRandom;
    private boolean mIsLoop;
    private Handler mHandler;
    private final int mDefaultTimeDelay = 1000;
    private final String mTypeTimeSong = "mm:ss";
    private final int mDefualtTimeSeebar = 0;

    public static Intent getPlayerIntent(Context context, Song song) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(GenreActivity.EXTRA_SONG, song);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        initComponent();
        registerEventListeners();
    }

    private void registerEventListeners() {
        mButtonBack.setOnClickListener(this);
        mButtonPre.setOnClickListener(this);
        mButtonPlay.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);

        mImageFavorite.setOnClickListener(this);
        mImageDownload.setOnClickListener(this);
        mImageLoop.setOnClickListener(this);
        mImageShuffle.setOnClickListener(this);
    }

    private void initViews() {
        mButtonBack = findViewById(R.id.button_back);
        mButtonPre = findViewById(R.id.button_pre);
        mButtonPlay = findViewById(R.id.button_play);
        mButtonNext = findViewById(R.id.button_next);

        mImageFavorite = findViewById(R.id.image_favorite_song);
        mImageDownload = findViewById(R.id.image_download);
        mImageLoop = findViewById(R.id.image_loop);
        mImageShuffle = findViewById(R.id.image_random);
        mImageSong = findViewById(R.id.image_song_player);

        mTextSongName = findViewById(R.id.text_song_name);
        mTextCurrentTime = findViewById(R.id.text_time_current);
        mTextEndTime = findViewById(R.id.text_time_end);

        mSeekBarSong = findViewById(R.id.seekbar_song);
    }

    private void initComponent() {
        mHandler = new Handler();
        mPresenter = new PlayerPresenter();
        mPresenter.setView(this);
        mSong = getIntent().getParcelableExtra(GenreActivity.EXTRA_SONG);
        setContextComponent(mSong);
        mSeekBarSong.setOnSeekBarChangeListener(mOnSeekBarChange);
    }

    private void setContextComponent(Song song) {
        if (song != null) {
            mSong = song;
            mTextSongName.setText(song.getTitle());
            Picasso.with(this)
                    .load(song.getArtworkUrl())
                    .placeholder(R.drawable.item_music_app)
                    .into(mImageSong);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                onBackPressed();
                finish();
                break;
            case R.id.button_pre:
                setTextSeekBar();
                mService.onPreviousMedia();
                break;
            case R.id.button_play:
                mService.onPauseMedia();
                break;
            case R.id.button_next:
                setTextSeekBar();
                mService.onNextMedia();
                break;
            case R.id.image_favorite_song:
                mService.onFavoriteSong();
                break;
            case R.id.image_download:
                mService.onDownloadSong();
                break;
            case R.id.image_loop:
                setImageLoop();
                mService.onLoopMedia();
                break;
            case R.id.image_random:
                setImageShuffle();
                mService.onRandomMedia();
                break;
            default:
                return;
        }
    }

    private Runnable mTimeCounter = new Runnable() {
        @Override
        public void run() {
            if (mService.isPlay()) {
                mService.getDuration();
            }
            updateUI();
        }
    };

    private void setTextSeekBar() {
        mTextCurrentTime.setText(getString(R.string.time_media));
        mTextEndTime.setText(getString(R.string.time_media));
        mSeekBarSong.setProgress(mDefualtTimeSeebar);
        setImagePauseSong();
    }

    @Override
    public void setImageShuffle() {
        if (!mIsRandom) {
            mImageShuffle.setImageResource(R.drawable.ic_loop_green);
        } else {
            mImageShuffle.setImageResource(R.drawable.ic_loop_while);
        }
        mIsRandom = !mIsRandom;
    }

    @Override
    public void setImagePauseSong() {
        if (!mIsPlay) {
            mButtonPlay.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mButtonPlay.setImageResource(android.R.drawable.ic_media_play);
        }
        mIsPlay = !mIsPlay;
    }

    @Override
    public void setImageLoop() {
        if (!mIsLoop) {
            mImageLoop.setImageResource(R.drawable.ic_random_green);
        } else {
            mImageLoop.setImageResource(R.drawable.ic_random_while);
        }
        mIsLoop = !mIsLoop;
    }

    private void updateUI() {
        mHandler.postDelayed(mTimeCounter, mDefaultTimeDelay);
    }

    private String convertToTime(long duration) {
        SimpleDateFormat sdf = new SimpleDateFormat(mTypeTimeSong, Locale.getDefault());
        String time = sdf.format(duration);
        return time;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
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
        mHandler.removeCallbacks(mTimeCounter);
        mPresenter.onStop();
        super.onStop();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalService binder = (MusicService.LocalService) iBinder;
            mService = binder.getService();
            mService.setInterface(PlayerActivity.this);
            mService.onPlayMedia(mSong);
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
        }
    };

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChange =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mService.setSeekTo(seekBar.getProgress());
                }
            };

    @Override
    public void setSong(Song song) {
        if (mService != null){
            if (!mService.isPlay()) {
                mService.updateNotification(song, true);
            }else {
                mService.updateNotification(song, false);
            }
        }
        setContextComponent(song);
    }

    @Override
    public void setImagePlay(String status) {
        switch (status) {
            case ACTION_MEDIA_PAUSE:
                mIsPlay = true;
                break;
            case ACTION_MEDIA_START:
                mIsPlay = false;
                break;
            case ACTION_MEDIA_FAIL:
                mIsPlay = true;
                Toast.makeText(this, getString(R.string.song_fail), Toast.LENGTH_SHORT).show();
                break;
        }
        setImagePauseSong();
    }

    @Override
    public void setTextTime(int current, int endTime) {
        mSeekBarSong.setMax(endTime);
        mSeekBarSong.setProgress(current);
        mTextCurrentTime.setText(convertToTime(current));
        mTextEndTime.setText(convertToTime(endTime));
    }

}
