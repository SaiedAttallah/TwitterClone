package com.cosmos.saiedattallah.twitterclone.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cosmos.saiedattallah.twitterclone.R;
import com.cosmos.saiedattallah.twitterclone.helpers.PatternEditableBuilder;
import com.cosmos.saiedattallah.twitterclone.helpers.RelativeTimeHelper;
import com.cosmos.saiedattallah.twitterclone.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by saeed on 23/08/17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private List<Tweet> tweets;
    private Context context;
    private ViewHolderListener listener;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
        this.listener = null;
    }

    private Context getContext() {
        return context;
    }

    public void setViewHolderListener(ViewHolderListener listener) {
        this.listener = listener;
    }

    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parentContext);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetsAdapter.ViewHolder holder, int position) {
        // get tweeet
        Tweet tweet = tweets.get(position);

        // get stuff from view holder
        ImageView ivProfileImage = holder.ivProfileImage;
        TextView tvUsername = holder.tvUsername;
        TextView tvBody = holder.tvBody;
        TextView tvScreenName = holder.tvScreenName;
        TextView tvPostDate = holder.tvPostDate;
        final TextView tvLikeCount = holder.tvLikeCount;
        ImageView ivReplyImage = holder.ivReplyImage;
        TextView tvRetweetCount = holder.tvRetweetCount;
        final ImageView ivRetweetImage = holder.ivRetweetImage;
        final ImageView ivFavoritedImage = holder.ivFavoritedImage;

        // set data up
        tvUsername.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String screenName) {
                                listener.launchProfileActivity(screenName);
                            }
                        }).into(tvBody);
        tvScreenName.setText(tweet.getUser().getScreenName());
        tvPostDate.setText(RelativeTimeHelper.getRelativeTimeAgo(tweet.getCreatedAt()));
        tvLikeCount.setText(tweet.getFavoriteCountString());
        tvRetweetCount.setText(tweet.getRetweetCountString());

        // clear out old image for recycle view
        ivProfileImage.setImageResource(android.R.color.transparent);
        ivProfileImage.setTag(tweet.getUser().getScreenName());
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.launchProfileActivity(view.getTag().toString());
            }
        });
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(7, 7)).fit().centerInside().into(ivProfileImage);

        ivReplyImage.setTag(tweet.getUser().getScreenName());
        ivReplyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEBUG", view.getTag().toString());
                listener.launchReplyDialog(view.getTag().toString());
            }
        });

        if (tweet.getFavorited()) {
            Picasso.with(getContext()).load(R.drawable.heart).fit().centerInside().into(ivFavoritedImage);
        } else {
            Picasso.with(getContext()).load(R.drawable.unliked).fit().centerInside().into(ivFavoritedImage);
        }
        ivFavoritedImage.setTag(tweet);
        ivFavoritedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tweet tweet = (Tweet) view.getTag();
                if (tweet.getFavorited()) {
                    listener.unfavoriteTweet(tweet.getUid());
                    Picasso.with(getContext()).load(R.drawable.unliked).fit().centerInside().into(ivFavoritedImage);
                    tweet.favoriteCount = tweet.favoriteCount - 1;
                    tweet.favorited = false;
                    tweet.save();
                    tvLikeCount.setText(Integer.toString(tweet.getFavoriteCount()));
                } else {
                    listener.favoriteTweet(tweet.getUid());
                    Picasso.with(getContext()).load(R.drawable.heart).fit().centerInside().into(ivFavoritedImage);
                    tweet.favoriteCount = tweet.favoriteCount + 1;
                    tweet.favorited = true;
                    tweet.save();
                    tvLikeCount.setText(Integer.toString(tweet.getFavoriteCount()));
                }
            }
        });

        ivRetweetImage.setTag(tweet.getUid());
        ivRetweetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.retweetTweet((Long) view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    public static interface ViewHolderListener {
        public void launchProfileActivity(String screenName);

        public void launchReplyDialog(String screenName);

        public void retweetTweet(long tweetId);

        public void favoriteTweet(long tweetId);

        public void unfavoriteTweet(long tweetId);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvScreenName;
        public TextView tvPostDate;
        public TextView tvLikeCount;
        public ImageView ivReplyImage;
        public TextView tvRetweetCount;
        public ImageView ivRetweetImage;
        public ImageView ivFavoritedImage;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = ButterKnife.findById(itemView, R.id.ivProfileImage);
            tvUsername = ButterKnife.findById(itemView, R.id.tvUsername);
            tvBody = ButterKnife.findById(itemView, R.id.tvBody);
            tvScreenName = ButterKnife.findById(itemView, R.id.tvScreenName);
            tvPostDate = ButterKnife.findById(itemView, R.id.tvPostDate);
            tvLikeCount = ButterKnife.findById(itemView, R.id.tvLikeCount);
            ivReplyImage = ButterKnife.findById(itemView, R.id.ivReply);
            tvRetweetCount = ButterKnife.findById(itemView, R.id.tvRetweetCount);
            ivRetweetImage = ButterKnife.findById(itemView, R.id.ivRetweet);
            ivFavoritedImage = ButterKnife.findById(itemView, R.id.ivLikes);
        }

        public interface ViewHolderListener {

        }
    }
}
