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
    private List<Song> mSongs;
    private int mPosition;
    private Song mSong;
    private static boolean mIsLocal;
    private boolean mIsRandom;
    private final int mDefualtRandom = 0;
    private IUpdateUi mIUpdateUi;
    private static MusicMediaPlayer mInstant;

    public static MusicMediaPlayer getInstant() {
        if (mInstant == null) {
            mInstant = new MusicMediaPlayer();
        }
        return mInstant;
    }

    public static MusicMediaPlayer getInstant(boolean local) {
        mIsLocal = local;
        if (mInstant == null) {
            mInstant = new MusicMediaPlayer();
        }
        return mInstant;
    }


    public void setLocal(boolean local) {
        mIsLocal = local;
    }

    public void setIUpdateUi(IUpdateUi IUpdateUi) {
        mIUpdateUi = IUpdateUi;
    }

    public Song getSong() {
        return mSong;
    }

    public void setSongs(List<Song> songs) {
        mSongs = songs;
    }

    public void setPosition(int position) {
        mPosition = position;
        mSong = mSongs.get(position);
        if (mIUpdateUi != null) {
            mIUpdateUi.setSong(mSong);
        }
        stopMedia();
    }

    public void onStartMedia() {
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
        if (mSong.isStreamable()) {
            try {
                String url;
                if (!mIsLocal) {
                    url = mSong.getUri() + ConstantApi.PLAY_URL;
                } else {
                    url = mSong.getArtworkUrl();
                }
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
                String url;
                if (!mIsLocal) {
                    url = mSong.getUri() + ConstantApi.PLAY_URL;
                } else {
                    url = mSong.getArtworkUrl();
                }
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
        if (mIUpdateUi != null) {
            mIUpdateUi.setSong(mSong);
        }
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
        mIUpdateUi.setTextTime(mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
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
            if (mPosition != random) {
                mPosition = random;
                mIsRandom = !mIsRandom;
            } else {
                randomMedia();
            }
        }
    }

    private MediaPlayer.OnPreparedListener mOnPrepare = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_START);
            mIUpdateUi.setVisibilityProgressBar();
            mediaPlayer.start();
        }
    };

    private MediaPlayer.OnCompletionListener mCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mIUpdateUi != null) {
                nextMedia();
                mIUpdateUi.setImagePlay(PlayerActivity.ACTION_MEDIA_PAUSE);
            }
        }
    };

    public void stopMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
