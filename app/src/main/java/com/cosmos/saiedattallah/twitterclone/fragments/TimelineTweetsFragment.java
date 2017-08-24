package com.cosmos.saiedattallah.twitterclone.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.cosmos.saiedattallah.twitterclone.App;
import com.cosmos.saiedattallah.twitterclone.helpers.NetworkHelper;
import com.cosmos.saiedattallah.twitterclone.models.Tweet;
import com.cosmos.saiedattallah.twitterclone.rest.TwitterClientProvider;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineTweetsFragment extends TweetsListFragment {
    private TwitterClientProvider twitterClientProvider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClientProvider = App.getRestClient();

        populateTimeline(1);
    }

    public void populateTimeline(long maxId) {
        if (NetworkHelper.isOnline() && NetworkHelper.isNetworkAvailable(getActivity())) {
            twitterClientProvider.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(response);
                    addAll(newTweets);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("DEBUG", "STATUS CODE = " + Integer.toString(statusCode));
                    Log.d("DEBUG", responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("DEBUG", "STATUS CODE = " + Integer.toString(statusCode));
                    Log.d("DEBUG", errorResponse.toString());
                }

                @Override
                public void onUserException(Throwable error) {
                    Log.d("DEBUG", error.toString());
                }
            });
        } else {
            ArrayList<Tweet> dbTweets = Tweet.getAll();
            addAll(dbTweets);
            Toast.makeText(getActivity(), "You're offline, using DB. Check your network connection",Toast.LENGTH_LONG).show();
        }
    }

}
