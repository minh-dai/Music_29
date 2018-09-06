package com.framgia.music_29.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.screen.home.HomeActivity;
import com.framgia.music_29.utils.ConstantApi;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashContract.Presenter mPresenter;
    private ProgressBar mProgressBar;
    private List<Song> mMusics;
    private List<Song> mAudios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initComponent();
        checkList();
    }

    private void initView() {
        mProgressBar = findViewById(R.id.progressBar);
    }

    private void initComponent() {
        mAudios = new ArrayList<>();
        mMusics = new ArrayList<>();
        mPresenter = new SplashPresenter();
        mPresenter.setView(this);
        mPresenter.loadDataMusics(ConstantApi.GENRE_ALL_MUSIC);
        mPresenter.loadDataAudios(ConstantApi.GENRE_ALL_AUDIO);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void checkList() {
        if (mAudios.size() > 0 && mMusics.size() > 0) {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);

            intent.putParcelableArrayListExtra(ConstantApi.GENRE_ALL_MUSIC,
                    (ArrayList<? extends Parcelable>) mMusics);

            intent.putParcelableArrayListExtra(ConstantApi.GENRE_ALL_AUDIO,
                    (ArrayList<? extends Parcelable>) mAudios);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDataError(Exception e) {
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDataMusics(List<Song> list) {
        mMusics.addAll(list);
    }

    @Override
    public void setDataAudios(List<Song> list) {
        mAudios.addAll(list);
    }
}
