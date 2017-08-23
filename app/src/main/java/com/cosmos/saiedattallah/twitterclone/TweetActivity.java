package com.cosmos.saiedattallah.twitterclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TweetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView contributorName = (TextView) findViewById(R.id.contributor_name);
        contributorName.setText(getIntent().getStringExtra("contributorName"));
        TextView tweetTime = (TextView) findViewById(R.id.tweet_time);
        tweetTime.setText(getIntent().getStringExtra("tweetTime"));
        TextView tweetText = (TextView) findViewById(R.id.tweet_txt);
        tweetText.setText(getIntent().getStringExtra("tweetText"));
    }
}
