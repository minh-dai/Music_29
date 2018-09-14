package com.framgia.music_29.screen.home;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.search.SearchActivity;
import com.framgia.music_29.screen.service.MusicService;
import com.framgia.music_29.utils.PassSongService;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View,
        View.OnClickListener, PassSongService {

    private ViewPagerAdapter mViewPagerAdapter;
    private HomeContract.Presenter mPresenter;
    private ConstraintLayout mLayoutHome;
    private TextView mTextSong;
    private ImageView mImageSongHome;
    private ImageView mImagePlayHome;
    private MusicService mService;
    private boolean mIsPlay;
    private boolean mIsBound;
    private boolean mLocal;
    private IPassSongOnline mIPassSongOnline;
    private IPassSongOffline mIPassSongOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initComponent();
        checkAndRequestPermissions();
    }

    private void initView() {
        mImageSongHome = findViewById(R.id.image_song_home);
        mTextSong = findViewById(R.id.text_song_home);
        mLayoutHome = findViewById(R.id.constraint_home);

        TabLayout tabLayout = findViewById(R.id.tablayout_home);
        ViewPager viewPager = findViewById(R.id.viewpager_home);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        mImagePlayHome = findViewById(R.id.image_play_home);
        findViewById(R.id.image_search).setOnClickListener(this);
        findViewById(R.id.image_pre_home).setOnClickListener(this);
        findViewById(R.id.image_play_home).setOnClickListener(this);
        findViewById(R.id.image_next_home).setOnClickListener(this);
    }

    private void initComponent() {
        mPresenter = new HomePresenter();
        mPresenter.setView(this);
    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_search:
                startActivity(SearchActivity.getSearchIntent(this, mService.getSong(), mService.isPlay(), mLocal));
                break;
            case R.id.image_pre_home:
                setImagePlay();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_PREVIOUS);
                break;
            case R.id.image_play_home:
                setImagePauseSong();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_STATE);
                break;
            case R.id.image_next_home:
                setImagePlay();
                onStartService(MusicService.ACTION_CHANGE_MEDIA_NEXT);
                break;
            default:
                break;
        }
    }

    private void setImagePlay(){
        mImagePlayHome.setEnabled(false);
        mIsPlay = false;
        setImagePauseSong();
    }

    private void onStartService(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        startService(intent);
    }

    public void setPassSongViewPaper(IPassSongOnline passSongViewPaper) {
        mIPassSongOnline = passSongViewPaper;
    }

    public void setIPassSongOffline(IPassSongOffline IPassSongOffline) {
        mIPassSongOffline = IPassSongOffline;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mService != null) {
            mLocal = mService.isLocal();
            passSong(mService.getSong(), mLocal);
            passStatus(!mService.isPlay());
            mService.setPassInterface(HomeActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mService != null) {
            mLocal = mService.isLocal();
            passSong(mService.getSong(), mLocal);
            passStatus(!mService.isPlay());
            mService.setPassInterface(HomeActivity.this);
        }
    }

    @Override
    public void passSong(Song song, boolean local) {
        if (song != null) {
            if (local) {
                byte[] images = song.getUriImage();
                if (images == null) {
                    mImageSongHome.setImageResource(R.drawable.item_music);
                } else {
                    mImageSongHome.setImageBitmap(
                            BitmapFactory.decodeByteArray(images, 0, song.getUriImage().length));
                }
            } else {
                Picasso.with(this)
                        .load(song.getArtworkUrl())
                        .placeholder(R.drawable.item_music_app)
                        .into(mImageSongHome);
            }
            mLocal = local;
            mIPassSongOnline.passSong(song);
            mIPassSongOffline.passSong(song);
            mLayoutHome.setVisibility(View.VISIBLE);
            mTextSong.setText(song.getTitle());
        } else {
            mLayoutHome.setVisibility(View.GONE);
        }
    }

    @Override
    public void passStatus(boolean isPlay) {
        mImagePlayHome.setEnabled(true);
        mIsPlay = isPlay;
        setImagePauseSong();
    }

    @Override
    public void setImagePauseSong() {
        if (!mIsPlay) {
            mIPassSongOnline.passStatus(false);
            mIPassSongOffline.passStatus(false);
            mImagePlayHome.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mIPassSongOnline.passStatus(true);
            mIPassSongOffline.passStatus(true);
            mImagePlayHome.setImageResource(android.R.drawable.ic_media_play);
        }
        mIsPlay = !mIsPlay;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.LocalService binder = (MusicService.LocalService) iBinder;
            mService = binder.getService();
            mService.setPassInterface(HomeActivity.this);
            mIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
        }
    };

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
        mLayoutHome.setVisibility(View.GONE);
        mPresenter.onStop();
        super.onStop();
    }

    public interface IPassSongOnline {
        void passSong(Song song);

        void passStatus(boolean isPLay);
    }

    public interface IPassSongOffline {
        void passSong(Song song);

        void passStatus(boolean isPLay);
    }
}
