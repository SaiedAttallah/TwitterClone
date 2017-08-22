package com.cosmos.saiedattallah.twitterclone;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements ActionBar.TabListener {
    private ActionBar actionbar;
    private Fragment fragment;
    private String fragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initActionBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (fragment == null) {
            fragment = Fragment.instantiate(this, HomeTweetsFragment.class.getName());
            fragmentTag = "home";
            ft.add(R.id.fg_home_tweets, fragment, fragmentTag);
        } else {
            if (tab.getPosition() == 0) {
                fragment = Fragment.instantiate(this,
                        HomeTweetsFragment.class.getName());
            } else {
                fragment = Fragment.instantiate(this, FollowersFragment.class.getName());
                fragmentTag = "user";
            }
            ft.replace(R.id.fg_home_tweets, fragment, fragmentTag);

        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            ft.detach(fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public void initActionBar() {
        actionbar = getSupportActionBar();
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        addTabs();
    }

    public void addTabs() {
        ActionBar.Tab home = actionbar.newTab().setText(R.string.home)
                .setTabListener(this);
        ActionBar.Tab user = actionbar.newTab().setText(R.string.followers)
                .setTabListener(this);
        actionbar.addTab(home);
        actionbar.addTab(user);
    }
}
