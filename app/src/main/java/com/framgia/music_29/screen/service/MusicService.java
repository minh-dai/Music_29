package com.framgia.music_29.screen.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.genre.GenreActivity;
import com.framgia.music_29.screen.home.online.OnlineFragment;
import com.framgia.music_29.screen.player.IUpdateUi;
import com.framgia.music_29.screen.player.PlayerActivity;
import com.framgia.music_29.utils.PassSongService;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service implements OnMusicListener {

    public static final String ACTION_CHANGE_MEDIA_NEXT = "ACTION_CHANGE_MEDIA_NEXT";
    public static final String ACTION_CHANGE_MEDIA_PREVIOUS = "ACTION_CHANGE_MEDIA_PREVIOUS";
    public static final String ACTION_CHANGE_MEDIA_STATE = "ACTION_CHANGE_MEDIA_STATE";
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String EXTRA_POSITION = "com.framgia.music_29.EXTRA_POSITION";
    public static final String EXTRA_LIST_SONG = "com.framgia.music_29.EXTRA_SONG";
    public static final String EXTRA_START_SERVICE = "com.framgia.music_29.EXTRA_START_SERVICE";
    private IBinder mBinder = new LocalService();
    private Notification mNotification;
    private RemoteViews mRemoteViews;
    private MusicMediaPlayer mMusicMediaPlayer;
    private Intent mIntent;
    private boolean mLocal;
    private static final int ID_NOTIFICATION = 1;
    private static final String ID = "id";

    public static Intent getInstance(Context context, List<Song> songs, int position,
            boolean local) {
        Intent intent = new Intent(context, MusicService.class);
        intent.setAction(EXTRA_START_SERVICE);
        intent.putParcelableArrayListExtra(EXTRA_LIST_SONG,
                (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(OnlineFragment.EXTRA_GENRE_LOCAL, local);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicMediaPlayer = MusicMediaPlayer.getInstant();
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
                case EXTRA_START_SERVICE:
                    getIntentExtra(intent);
                    break;
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

    private void getIntentExtra(Intent intent) {
        mLocal = intent.getBooleanExtra(OnlineFragment.EXTRA_GENRE_LOCAL, false);
        mMusicMediaPlayer.setLocal(mLocal);
        List songs = intent.getParcelableArrayListExtra(EXTRA_LIST_SONG);
        int position = intent.getIntExtra(EXTRA_POSITION, 0);
        setMusicMediaPlayer(songs, position);
    }

    private void setMusicMediaPlayer(List<Song> songs, int position) {
        mMusicMediaPlayer = MusicMediaPlayer.getInstant();
        mMusicMediaPlayer.setSongs(songs);
        mMusicMediaPlayer.setPosition(position);
    }

    public void getDuration() {
        mMusicMediaPlayer.getTextDuration();
    }

    public void setInterface(IUpdateUi iUpdateUi) {
        mMusicMediaPlayer.setIUpdateUi(iUpdateUi);
    }

    public void setPassInterface(PassSongService passInterface) {
        mMusicMediaPlayer.setPassSongService(passInterface);
    }

    @Override
    public void onDestroy() {
        mMusicMediaPlayer.stopMedia();
        super.onDestroy();
    }

    @Override
    public void onPlayMedia(Song song) {
        mMusicMediaPlayer.onStartMedia();
        mMusicMediaPlayer.playMediaPre(song);
        if (mNotification == null) {
            createNotification(song, true);
        }
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

    public boolean isLocal() {
        return mLocal;
    }

    @Override
    public Song getSong() {
        return mMusicMediaPlayer.getSong();
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
            mMusicMediaPlayer.passStatusNotifi(true);
        } else {
            mRemoteViews.setImageViewResource(R.id.button_play_notification,
                    android.R.drawable.ic_media_pause);
            mMusicMediaPlayer.passStatusNotifi(false);
        }

        if (mLocal) {
            byte[] images = song.getUriImage();
            if (images != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
                mRemoteViews.setImageViewBitmap(R.id.image_notification, bitmap);
            } else {
                mRemoteViews.setImageViewResource(R.id.image_notification, R.drawable.item_music);
            }
        } else {
            Picasso.with(getApplicationContext())
                    .load(song.getArtworkUrl())
                    .placeholder(R.drawable.item_music)
                    .into(mRemoteViews, R.id.image_notification, ID_NOTIFICATION, mNotification);
        }
        mIntent.putExtra(GenreActivity.EXTRA_SONG, mMusicMediaPlayer.getSong());
        PendingIntent pIntent =
                PendingIntent.getActivities(this, ID_NOTIFICATION, new Intent[] { mIntent },
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.layoutNotifi, pIntent);

        startForeground(ID_NOTIFICATION, mNotification);
    }

    private void pendingButtonPre() {
        Intent intent1 = new Intent(this, MusicService.class);
        intent1.setAction(ACTION_CHANGE_MEDIA_PREVIOUS);
        PendingIntent pending =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intent1, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.button_pre_notification, pending);
    }

    private void pendingPlay() {
        Intent intentActionStop = new Intent(this, MusicService.class);
        intentActionStop.setAction(ACTION_CHANGE_MEDIA_STATE);
        PendingIntent pStopSelf =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intentActionStop,
                        0);
        mRemoteViews.setOnClickPendingIntent(R.id.button_play_notification, pStopSelf);
    }

    private void pendingButtonNext() {
        Intent intentNext = new Intent(this, MusicService.class);
        intentNext.setAction(ACTION_CHANGE_MEDIA_NEXT);
        PendingIntent pendingIntentNext =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intentNext, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.button_next_notification, pendingIntentNext);
    }

    private void pedingButtonStop() {
        Intent intentStop = new Intent(this, MusicService.class);
        intentStop.setAction(ACTION_STOP_SERVICE);
        PendingIntent pendingIntentStop =
                PendingIntent.getService(this, (int) System.currentTimeMillis(), intentStop, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.button_stop_notification, pendingIntentStop);
    }

    private void pendingNotifi() {
        mIntent = new Intent(this, PlayerActivity.class);
        mIntent.putExtra(GenreActivity.EXTRA_SONG, mMusicMediaPlayer.getSong());
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivities(this, (int) System.currentTimeMillis(),
                        new Intent[] { mIntent }, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.layoutNotifi, pendingIntent);
    }

    private void createNotification(final Song song, boolean play) {
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.item_notification);

        pendingButtonPre();
        pendingNotifi();
        pendingPlay();
        pendingButtonNext();
        pedingButtonStop();

        Notification.Builder notificationBuilder =
                new Notification.Builder(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification = notificationBuilder.setSmallIcon(R.drawable.item_music)
                    .setDefaults(Notification.FLAG_NO_CLEAR)
                    .setContent(mRemoteViews)
                    .build();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel =
                    new NotificationChannel(ID, name, importance);
            mNotification = notificationBuilder.setSmallIcon(R.drawable.item_music)
                    .setChannelId(ID)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager.notify(ID_NOTIFICATION, mNotification);
        }

        updateNotification(song, play);
    }
}
