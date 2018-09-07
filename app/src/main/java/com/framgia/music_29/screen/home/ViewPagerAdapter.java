package com.framgia.music_29.screen.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.framgia.music_29.R;
import com.framgia.music_29.screen.home.offline.OfflineFragment;
import com.framgia.music_29.screen.home.online.OnlineFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGER_NUMBERS = 2;
    private static final int ONLINE_TAB = 0;
    private static final int OFFLINE_TAB = 1;
    private Context mContext;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case ONLINE_TAB:
                return OnlineFragment.newInstance();
            case OFFLINE_TAB:
                return OfflineFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGER_NUMBERS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case ONLINE_TAB:
                return mContext.getString(R.string.name_tab_online);
            case OFFLINE_TAB:
                return mContext.getString(R.string.name_tab_offline);
            default:
                return null;
        }
    }
}

