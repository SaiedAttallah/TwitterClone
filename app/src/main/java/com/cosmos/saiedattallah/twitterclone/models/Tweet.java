package com.cosmos.saiedattallah.twitterclone.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saeed on 23/08/17.
 */

@Table(name = "Tweets")
@Parcel(analyze = { Tweet.class})
public class Tweet extends Model {
    @Column(name = "Body")
    public String body;

    @Column(name = "UID", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long uid;

    @Column(name = "CreatedAt")
    public String createdAt;

    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.NO_ACTION)
    public User user;

    @Column(name = "FavoritedCount")
    public int favoriteCount;

    @Column(name = "RetweetCount")
    public int retweetCount;

    @Column(name = "Favorited")
    public boolean favorited;

    public int getRetweetCount() { return retweetCount; };

    public boolean getFavorited() { return favorited; }

    public String getRetweetCountString() { return Integer.toString(retweetCount); }

    public User getUser(){
        return user;
    }

    public long getUid() {
        return uid;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getFavoriteCount() { return favoriteCount; }

    public String getFavoriteCountString() { return Integer.toString(favoriteCount); }

    public Tweet(){
        super();
    }

    public static Tweet findOrCreateFromJSONObect(JSONObject jsonObject){
        long rid = 0;
        try {
            rid = jsonObject.getLong("id");
            Tweet existingTweet = new Select().from(Tweet.class).where("uid = ?",rid).executeSingle();

            if (existingTweet != null) {
                return existingTweet;
            } else {
                Tweet tweet = Tweet.fromJSONObject(jsonObject);
                tweet.save();
                return tweet;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Tweet fromJSONObject(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.user = User.findOrCreateFromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.findOrCreateFromJSONObect(tweetJson);
                if (tweet != null) {
                    tweets.add(i,tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    public static ArrayList<Tweet> getAll(){
        ArrayList<Tweet> tweetArrayList = new ArrayList<Tweet>();
        List<Tweet> tweetList = new Select().from(Tweet.class).orderBy("uid DESC").limit(200).execute();
        for(int i = 0; i < tweetList.size(); i++) {
            tweetArrayList.add(tweetList.get(i));
        }
        return tweetArrayList;
    }

}
