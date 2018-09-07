package com.framgia.music_29.screen.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.framgia.music_29.R;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private ViewPagerAdapter mViewPagerAdapter;
    private HomeContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initComponent();
    }

    private void initView() {
        TabLayout tabLayout = findViewById(R.id.tablayout_home);
        ViewPager viewPager = findViewById(R.id.viewpager_home);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initComponent() {
        mPresenter = new HomePresenter();
        mPresenter.setView(this);
    }
}
