package com.framgia.music_29.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class LoadMore extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLayoutManager;
    private boolean mLoading;

    public LoadMore(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mLayoutManager != null) {
            int visibleThreshold = 5;
            int totalItemCount = mLayoutManager.getItemCount();
            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            int firstVisiableItem = mLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = mLayoutManager.getChildCount();

            if (mLoading && totalItemCount > (lastVisibleItem + visibleThreshold)) {
                mLoading = false;
            }
            if (!mLoading && (visibleItemCount + firstVisiableItem) >= totalItemCount){
                if (mLayoutManager != null) {
                    OnLoadMoreItem();
                }
                mLoading = true;
            }
        }
    }

    protected abstract void OnLoadMoreItem();
}
