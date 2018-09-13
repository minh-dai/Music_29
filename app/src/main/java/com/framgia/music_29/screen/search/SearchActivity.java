package com.framgia.music_29.screen.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.framgia.music_29.R;
import com.framgia.music_29.data.model.Song;
import com.framgia.music_29.utils.OnClickItemListener;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements SearchContract.View, View.OnClickListener, OnClickItemListener {

    private RecyclerView mRecyclerView;
    private ImageView mImageBack;
    private ImageView mImageSearch;
    private ImageView mImageFail;
    private EditText mEditSearch;
    private ProgressBar mProgressBar;
    private SearchContract.Presenter mPresenter;
    private SearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        initComponents();
        registerEventListeners();
    }

    private void registerEventListeners() {
        mImageBack.setOnClickListener(this);
        mImageSearch.setOnClickListener(this);
    }

    private void initComponents() {
        mPresenter = new SearchPresenter();
        mPresenter.setView(this);
        mAdapter = new SearchAdapter(this);
        mAdapter.setClickItemListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.recylerview_search);
        mImageBack = findViewById(R.id.image_back_search);
        mImageSearch = findViewById(R.id.image_search);
        mImageFail = findViewById(R.id.image_fail);
        mEditSearch = findViewById(R.id.edit_search);
        mProgressBar = findViewById(R.id.progressbar_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back_search:
                onBackPressed();
                finish();
                break;
            case R.id.image_search:
                searchSong();
                break;
            default:
                break;
        }
    }

    private void searchSong() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        mImageFail.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.loadSongsSearch(mEditSearch.getText().toString());
    }

    @Override
    public void setSongSuccess(List<Song> songs) {
        if (songs.size() > 0) {
            mImageFail.setVisibility(View.GONE);
            mAdapter.setSongs(songs);
        } else {
            mImageFail.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.not_find_data), Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadSongsSearchError(Exception e) {
        mImageFail.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onClick(List<Song> songs, int position) {
        Toast.makeText(this, songs.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}
