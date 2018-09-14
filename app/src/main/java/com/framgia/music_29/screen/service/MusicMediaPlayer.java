package com.framgia.music_29.screen.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.player.IUpdateUi;
import com.framgia.music_29.screen.player.PlayerActivity;
import com.framgia.music_29.utils.ConstantApi;
import com.framgia.music_29.utils.PassSongService;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MusicMediaPlayer {
    private MediaPlayer mMediaPlayer;
    private List<Song> mSongs;
    private int mPosition;
    private Song mSong;
    private boolean mIsLocal;
    private boolean mIsRandom;
    private final int mDefualtRandom = 0;
    private IUpdateUi mIUpdateUi;
    private PassSongService mPassSongService;
    private static MusicMediaPlayer mInstant;

    public static MusicMediaPlayer getInstant() {
        if (mInstant == null) {
            mInstant = new MusicMediaPlayer();
        }
        return mInstant;
    }

    public MusicMediaPlayer() {
        onStartMedia();
    }

    public void setPassSongService(PassSongService passSongService) {
        mPassSongService = passSongService;
    }

    public void setLocal(boolean local) {
        mIsLocal = local;
    }

    public boolean getLocal() {
        return mIsLocal;
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
                passSong(mSong,  mIsLocal);
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(url);
                mMediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mMediaPlayer.stop();
            passImagePlay(PlayerActivity.ACTION_MEDIA_FAIL);
        }
    }

    public void passStatusNotifi(boolean status) {
        mPassSongService.passStatus(status);
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
            passImagePlay(PlayerActivity.ACTION_MEDIA_FAIL);
        }
    }

    private void updateSong() {
        mSong = mSongs.get(mPosition);
        mMediaPlayer.pause();
        if (mIUpdateUi != null) {
            mIUpdateUi.setSong(mSong);
        }
         passSong(mSong,  mIsLocal);

    }

    public void pauseMedia() {
        if (isPlay()) {
            mMediaPlayer.pause();
            passImagePlay(PlayerActivity.ACTION_MEDIA_PAUSE);
            passStatus(isPlay());
        } else {
            mMediaPlayer.start();
            passImagePlay(PlayerActivity.ACTION_MEDIA_START);
            passStatus(!isPlay());
        }
    }

    public void nextMedia() {
        if (mPosition < mSongs.size() - 1) {
            ++mPosition;
            updateSong();
            playMediaPre();
        }
    }

    private void passImagePlay(String action){
        if (mIUpdateUi!= null){
            mIUpdateUi.setImagePlay(action);
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

    private void passStatus(boolean status){
        if (mPassSongService != null) {
            mPassSongService.passStatus(status);
        }
    }

    private void passSong(Song song, boolean isLocal){
        if (mPassSongService != null) {
            mPassSongService.passSong(song,  isLocal);
        }
    }

    private MediaPlayer.OnPreparedListener mOnPrepare = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            passImagePlay(PlayerActivity.ACTION_MEDIA_START);
            passStatus(false);
            mIUpdateUi.setVisibilityProgressBar();
        }
    };

    private MediaPlayer.OnCompletionListener mCompletion = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            nextMedia();
            passImagePlay(PlayerActivity.ACTION_MEDIA_PAUSE);
            passStatus(true);
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
