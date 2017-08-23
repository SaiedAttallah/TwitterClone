package com.cosmos.saiedattallah.twitterclone;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Created by SaiedAttallah on 8/23/2017.
 */

public class TimelineTweets extends HomeTweetsFragment {
    private List<Status> tweetsList;

    @Override
    public void refresh() {
        new RetrieveTweets().execute(paging);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Status status = tweetsList.get(position);
        Intent intent = new Intent(getActivity(), TweetActivity.class);
        intent.putExtra("tweetText", status.getText());
        intent.putExtra("contributorName", status.getUser().getScreenName());
        intent.putExtra("owner", status.getUser().getName());
        intent.putExtra("tweetTime", status.getCreatedAt().toString());
        startActivity(intent);
    }

    private class RetrieveTweets extends AsyncTask<Paging, Integer, List<Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(Paging... params) {
            try {
                tweetsList = twitter.getHomeTimeline(params[0]);
                return tweetsList;
            } catch (TwitterException e) {
                Log.e("Twitter", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> result) {
            if (result != null) {
                setListAdapter(new TweetAdapter(getActivity()
                        .getApplicationContext(), R.layout.activity_tweet, result));
            }
        }

    }
}
