package com.cosmos.saiedattallah.twitterclone.rest;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * Created by saeed on 23/08/17.
 */

public class TwitterClientProvider extends OAuthBaseClient {

    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "VfSobidUqadeRAz49OTHiA0m8";
    public static final String REST_CONSUMER_SECRET = "OLXvmWBUsA4QcxDo1x72XeZAVIx5qA2znVopX2p2AoEgv7xiVO"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://glhpeep";

    public TwitterClientProvider(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count",25);
        if (maxId != 1) {
            params.put("max_id",maxId);
        }
        client.get(apiUrl, params, handler);
    }

    public void postStatusUpdate(RequestParams params, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        client.post(apiUrl,params, handler);
    }

    public void getMentions(long maxId, JsonHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count",25);
        client.get(apiUrl, params, handler);
    }

    public void getCurrentUserInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, handler);
    }

    public void getUserTimeline(String screenName, long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("screen_name",screenName);
        if (maxId != 1) {
            params.put("max_id", maxId);
        }
        params.put("count",25);
        client.get(apiUrl, params, handler);
    }

    public void getOtherUserInfo(String screenName, AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name",screenName);
        client.get(apiUrl, params, handler);
    }


    public void postRetweet(Long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/" + Long.toString(tweetId) + ".json");
        client.post(apiUrl, null, handler);
    }


    public void likeTweet(Long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id",tweetId);
        client.post(apiUrl, params, handler);
    }

    public void unlikeTweet(Long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id",tweetId);
        client.post(apiUrl, params, handler);
    }
}
