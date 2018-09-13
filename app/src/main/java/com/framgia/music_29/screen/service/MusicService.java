package com.framgia.music_29.screen.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.data.source.remote.DownloadDataSource;
import com.framgia.music_29.screen.player.IUpdateUi;
import com.framgia.music_29.screen.player.PlayerActivity;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MusicService extends Service implements onStatusListener {

    private static final String ACTION_CHANGE_MEDIA_NEXT = "ACTION_CHANGE_MEDIA_NEXT";
    private static final String ACTION_CHANGE_MEDIA_PREVIOUS = "ACTION_CHANGE_MEDIA_PREVIOUS";
    private static final String ACTION_CHANGE_MEDIA_STATE = "ACTION_CHANGE_MEDIA_STATE";
    private final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String EXTRA_SONG = "song";
    private IBinder mBinder = new LocalService();
    private Notification mNotification;
    private RemoteViews mRemoteViews;
    private static MusicMediaPlayer mMusicMediaPlayer;
    private Intent mIntent;
    private static boolean mLocal;
    private static final int ID_NOTIFICATION = 1;

    public static Intent getInstance(Context context, List<Song> songs, int position) {
        Intent intent = new Intent(context, MusicService.class);
        mMusicMediaPlayer = MusicMediaPlayer.getInstant(songs, position);
        return intent;
    }

    public static Intent getInstance(Context context,boolean local ,List<Song> songs, int position) {
        mLocal = local;
        Intent intent = new Intent(context, MusicService.class);
        mMusicMediaPlayer = MusicMediaPlayer.getInstant(songs, position , local);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicMediaPlayer.onStartMedia();
        createNotification(mMusicMediaPlayer.getSong(), true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_CHANGE_MEDIA_PREVIOUS:
                    onPreviousMedia();
                    break;
                case ACTION_CHANGE_MEDIA_STATE:
                    onPauseMedia();
                    break;
                case ACTION_CHANGE_MEDIA_NEXT:
                    onNextMedia();
                    break;
                case ACTION_STOP_SERVICE:
                    onStopService();
                    break;
            }
        }
        return START_STICKY;
    }

    public void onDownloadSong(String url , Context context) {
        new DownloadDataSource(context).execute(url);
    }

    public void onFavoriteSong() {

    }

    public void getDuration() {
        mMusicMediaPlayer.getTextDuration();
    }

    public void setInterface(IUpdateUi iUpdateUi) {
        mMusicMediaPlayer.setIUpdateUi(iUpdateUi);
    }

    @Override
    public void onDestroy() {
        mMusicMediaPlayer.stopMedia();
        super.onDestroy();
    }

    @Override
    public void onPlayMedia(Song song) {
        mMusicMediaPlayer.playMediaPre(song);
    }

    @Override
    public void onPauseMedia() {
        mMusicMediaPlayer.pauseMedia();
        if (mMusicMediaPlayer.isPlay()) {
            updateNotification(mMusicMediaPlayer.getSong(), true);
        } else {
            updateNotification(mMusicMediaPlayer.getSong(), false);
        }
    }

    @Override
    public void onPreviousMedia() {
        mMusicMediaPlayer.previousMedia();
        updateNotification(mMusicMediaPlayer.getSong(), true);
    }

    @Override
    public void onNextMedia() {
        mMusicMediaPlayer.nextMedia();
        updateNotification(mMusicMediaPlayer.getSong(), true);
    }

    @Override
    public void onStopService() {
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onLoopMedia() {
        mMusicMediaPlayer.loopMedia();
    }

    @Override
    public void onRandomMedia() {
        mMusicMediaPlayer.randomMedia();
    }

    @Override
    public void setSeekTo(int seekBarProgress) {
        mMusicMediaPlayer.setSeekTo(seekBarProgress);
    }

    @Override
    public boolean isPlay() {
        return mMusicMediaPlayer.isPlay();
    }

    @Override
    public void setSong(Song song) {
        mMusicMediaPlayer.setSong(song);
    }

    public class LocalService extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void updateNotification(Song song, boolean play) {
        mRemoteViews.setTextViewText(R.id.text_song_name_notifi, song.getTitle());
        // play = flase thi dung
        if (!play) {
            mRemoteViews.setImageViewResource(R.id.button_play_notification,
                    android.R.drawable.ic_media_play);
        } else {
            mRemoteViews.setImageViewResource(R.id.button_play_notification,
                    android.R.drawable.ic_media_pause);
        }

        if (mLocal){
            byte[] images = song.getUriImage();
            if(images != null){
                Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
                mRemoteViews.setImageViewBitmap(R.id.image_notification, bitmap);
            }else {
                mRemoteViews.setImageViewResource(R.id.image_notification, R.drawable.item_music);
            }
        }else {
            Picasso.with(getApplicationContext())
                    .load(song.getArtworkUrl())
                    .placeholder(R.drawable.item_music)
                    .into(mRemoteViews, R.id.image_notification, ID_NOTIFICATION, mNotification);
        }
        mIntent.putExtra(EXTRA_SONG, mMusicMediaPlayer.getSong());
        PendingIntent pIntent =
                PendingIntent.getActivities(this, ID_NOTIFICATION, new Intent[] { mIntent },
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.layoutNotifi, pIntent);

        startForeground(ID_NOTIFICATION, mNotification);
    }

    private PendingIntent pendingButtonPre() {
        Intent intent1 = new Intent(this, MusicService.class);
        intent1.setAction(ACTION_CHANGE_MEDIA_PREVIOUS);
        PendingIntent pendingIntent1 =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intent1, 0);
        return pendingIntent1;
    }

    private PendingIntent pendingPlay() {
        Intent intentActionStop = new Intent(this, MusicService.class);
        intentActionStop.setAction(ACTION_CHANGE_MEDIA_STATE);
        PendingIntent pStopSelf =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intentActionStop,
                        0);
        return pStopSelf;
    }

    private PendingIntent pendingButtonNext() {
        Intent intentNext = new Intent(this, MusicService.class);
        intentNext.setAction(ACTION_CHANGE_MEDIA_NEXT);
        PendingIntent pendingIntentNext =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intentNext, 0);
        return pendingIntentNext;
    }

    private PendingIntent pedingButtonStop() {
        Intent intentStop = new Intent(this, MusicService.class);
        intentStop.setAction(ACTION_STOP_SERVICE);
        PendingIntent pendingIntentStop =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intentStop, 0);
        return pendingIntentStop;
    }

    private PendingIntent pendingNotifi() {
        mIntent = new Intent(this, PlayerActivity.class);
        mIntent.putExtra(EXTRA_SONG, mMusicMediaPlayer.getSong());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivities(this, (int) System.currentTimeMillis(),
                        new Intent[] { mIntent }, 0);
        return pendingIntent;
    }

    private void createNotification(final Song song, boolean play) {
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.item_notification);

        mRemoteViews.setOnClickPendingIntent(R.id.button_pre_notification, pendingButtonPre());
        mRemoteViews.setOnClickPendingIntent(R.id.layoutNotifi, pendingNotifi());
        mRemoteViews.setOnClickPendingIntent(R.id.button_play_notification, pendingPlay());
        mRemoteViews.setOnClickPendingIntent(R.id.button_next_notification, pendingButtonNext());
        mRemoteViews.setOnClickPendingIntent(R.id.button_stop_notification, pedingButtonStop());

        Notification.Builder notificationBuilder =
                new Notification.Builder(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification = notificationBuilder.setSmallIcon(R.drawable.item_music)
                    .setDefaults(Notification.FLAG_NO_CLEAR)
                    .setContent(mRemoteViews)
                    .build();
        }
        updateNotification(song, play);
    }
}
