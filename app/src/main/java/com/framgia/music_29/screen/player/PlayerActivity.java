package com.framgia.music_29.screen.player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.framgia.music_29.R;

public class PlayerActivity extends AppCompatActivity implements PlayerContract.View,
        View.OnClickListener {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                onBackPressed();
                finish();
                break;
            case R.id.button_pre:
                break;
            case R.id.button_play:
                break;
            case R.id.button_next:
                break;
            case R.id.image_favorite_song:
                break;
            case R.id.image_download:
                break;
            case R.id.image_loop:
                break;
            case R.id.image_random:
                break;
            default:
                return;
        }
    }

    private void initComponent(){
        mPresenter = new PlayerPresenter();
        mPresenter.setView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }
}
