package com.cosmos.saiedattallah.twitterclone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

import twitter4j.Paging;
import twitter4j.Twitter;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class HomeTweetsFragment extends ListFragment {

    protected Twitter twitter;
    protected Paging paging;


    public HomeTweetsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeAuthHelper();
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void initializeAuthHelper() {
        twitter = TwitterAuthHelper.getTwitterInstance();
        paging = new Paging(1, 20);
    }

    public abstract void refresh();

}
