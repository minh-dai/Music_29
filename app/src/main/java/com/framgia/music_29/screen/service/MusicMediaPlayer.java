package com.framgia.music_29.screen.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.player.IUpdateUi;
import com.framgia.music_29.screen.player.PlayerActivity;
import com.framgia.music_29.utils.ConstantApi;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MusicMediaPlayer {
    private MediaPlayer mMediaPlayer;
    private static List<Song> mSongs;
    private static int mPosition;
    private Song mSong;
    private boolean mIsRandom;
    private final int mDefualtRandom = 0;
    private IUpdateUi mIUpdateUi;
    private static MusicMediaPlayer mInstant;

    public static MusicMediaPlayer getInstant(List<Song> songs, int position) {
        if (mInstant == null){
            mInstant = new MusicMediaPlayer(songs , position);
        }
        return mInstant;
    }

    public MusicMediaPlayer(List<Song> songs, int position) {
        mSongs = songs;
        mPosition = position;
        mSong = mSongs.get(mPosition);
    }

    public void setIUpdateUi(IUpdateUi IUpdateUi) {
        mIUpdateUi = IUpdateUi;
    }

    public Song getSong() {
        return mSong;
    }

    public void onStartMedia(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(mOnPrepare);
        mMediaPlayer.setOnCompletionListener(mCompletion);
    }

    public void previousMedia() {
        if (mPosition != 0) {
            --mPosition;
            updateSong();
            playMediaPre();
        }
    }

    public void playMediaPre(Song song) {
        mSong = song;
        mPosition = mSongs.indexOf(song);
        if (mSong.isStreamable()) {
            try {
                String url = mSong.getUri() + ConstantApi.PLAY_URL;
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mMediaPlayer.stop();
            mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_FAIL);
        }
    }

    public void playMediaPre() {
        if (mSong.isStreamable()) {
            try {
                String url = mSong.getUri() + ConstantApi.PLAY_URL;
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mMediaPlayer.stop();
            mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_FAIL);
        }
    }

    private void updateSong() {
        mSong = mSongs.get(mPosition);
        mIUpdateUi.setSong(mSong);
    }

    public void pauseMedia() {
        if (isPlay()) {
            mMediaPlayer.pause();
            mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_PAUSE);
        } else {
            mMediaPlayer.start();
            mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_START);
        }
    }

    public void nextMedia() {
        if (mPosition < mSongs.size() - 1) {
            ++mPosition;
            updateSong();
            playMediaPre();
        }
    }

    public boolean isPlay() {
        return mMediaPlayer.isPlaying();
    }

    public void setSeekTo(int input) {
        mMediaPlayer.seekTo(input);
    }

    public void getTextDuration() {
        mIUpdateUi.setTextTime(mMediaPlayer.getCurrentPosition() , mMediaPlayer.getDuration());
    }

    public void loopMedia() {
        if (mMediaPlayer.isLooping()) {
            mMediaPlayer.setLooping(false);
        } else {
            mMediaPlayer.setLooping(true);
        }
    }

    public void randomMedia() {
        if (!mIsRandom) {
            Random r = new Random();
            int random = r.nextInt(mSongs.size() - 1) + mDefualtRandom;
            if (mPosition != random){
                mPosition = random;
                mIsRandom = !mIsRandom;
            }else {
                randomMedia();
            }
        }
    }

    private MediaPlayer.OnPreparedListener mOnPrepare = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mIUpdateUi.setSong(mSong);
            mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_START);
            mediaPlayer.start();
        }
    };

    private MediaPlayer.OnCompletionListener mCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            nextMedia();
            mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_PAUSE);
        }
    };

    public void stopMedia(){
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void setSong(Song song) {
        mPosition = mSongs.indexOf(song);
        mSong = song;
        playMediaPre();
    }
}
