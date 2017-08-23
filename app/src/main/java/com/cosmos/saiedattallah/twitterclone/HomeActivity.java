package com.cosmos.saiedattallah.twitterclone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

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
            fragment = Fragment.instantiate(this, TimelineTweets.class.getName());
            fragmentTag = "home";
            ft.add(R.id.home_activity_layout, fragment, fragmentTag);
        } else {
            if (tab.getPosition() == 0) {
                fragment = Fragment.instantiate(this,
                        TimelineTweets.class.getName());
            } else {
                fragment = Fragment.instantiate(this, FollowersFragment.class.getName());
                fragmentTag = "user";
            }
            ft.replace(R.id.home_activity_layout, fragment, fragmentTag);

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
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setDisplayShowTitleEnabled(true);
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
