package com.cosmos.saiedattallah.twitterclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import twitter4j.Status;

/**
 * Created by SaiedAttallah on 8/23/2017.
 */

public class TweetAdapter extends BaseAdapter {
    private List<Status> list;
    private int layout;
    private Context ctx;

    public TweetAdapter(Context context, int resource, List<Status> tweets) {
        list = tweets;
        layout = resource;
        ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }
        TextView contributorName = (TextView) convertView.findViewById(R.id.contributor_name);
        TextView tweetTime = (TextView) convertView.findViewById(R.id.tweet_time);
        TextView tweetText = (TextView) convertView.findViewById(R.id.tweet_txt);
        Status current = list.get(position);
        contributorName.setText(current.getUser().getName());
        tweetTime.setText(current.getCreatedAt().toString());
        tweetText.setText(current.getText());

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
