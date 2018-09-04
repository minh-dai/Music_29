package com.framgia.music_29.screen.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.framgia.music_29.R;
import com.framgia.music_29.screen.home.HomeActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private static final long DELAY_TIME = 3000;
    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initComponent();
        delayTime();
    }

    private void initComponent() {
        mPresenter = new SplashPresenter();
        mPresenter.setView(this);
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

    private void delayTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);

    }
}
