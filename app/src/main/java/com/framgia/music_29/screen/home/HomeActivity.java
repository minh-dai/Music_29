package com.framgia.music_29.screen.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.framgia.music_29.R;
import com.framgia.music_29.screen.search.SearchActivity;

public class HomeActivity extends AppCompatActivity implements HomeContract.View,
        View.OnClickListener {

    private ViewPagerAdapter mViewPagerAdapter;
    private HomeContract.Presenter mPresenter;
    private ImageView mImageSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initComponent();
        registerEventListener();
    }

    private void registerEventListener() {
        mImageSearch.setOnClickListener(this);
    }

    private void initView() {
        TabLayout tabLayout = findViewById(R.id.tablayout_home);
        ViewPager viewPager = findViewById(R.id.viewpager_home);
        mImageSearch = findViewById(R.id.image_search);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initComponent() {
        mPresenter = new HomePresenter();
        mPresenter.setView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_search:
                startActivity(SearchActivity.getSearchIntent(this));
                break;
            default:
                break;
        }
    }
}
