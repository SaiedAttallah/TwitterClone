package com.cosmos.saiedattallah.twitterclone.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cosmos.saiedattallah.twitterclone.fragments.FollowersFragment;
import com.cosmos.saiedattallah.twitterclone.fragments.TimelineTweetsFragment;

/**
 * Created by saeed on 23/08/17.
 */

public class TwitterPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = { "Home", "Followers"};

    public TwitterPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new TimelineTweetsFragment();
        } else if (position == 1) {
            return new FollowersFragment();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
