package com.cosmos.saiedattallah.twitterclone;

import android.os.AsyncTask;
import android.util.Log;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by SaiedAttallah on 8/21/2017.
 */

public class TwitterAuthHelper {
    private static Twitter twitter;
    private static final String CALLBACK_URL = "http://oauth.gmodules.com/gadgets/oauthcallback";
    private AccessToken accessToken;
    private RequestToken requestToken;

    public TwitterAuthHelper(String consumerKey, String consumerSecret) {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
    }

    public String getAuthorizationURL() {
        try {
            return new AuthURLGetter().execute().get();
        } catch (Exception e) {
            Log.e("twitter", e.getMessage());
        }
        return null;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken, String tokenSecret) {
        this.accessToken = new AccessToken(accessToken, tokenSecret);
    }

    public void setAccessToken(String verifier) {
        try {
            accessToken = new AccessTokenGetter().execute(verifier).get();
        } catch (Exception e) {
            Log.e("Twitter", e.getMessage());
        }
    }

    public void twitterInit(AccessToken accessToken) {
        setAccessToken(accessToken.getToken(), accessToken.getTokenSecret());
        twitterInit();
    }

    public void twitterInit() {
        twitter.setOAuthAccessToken(accessToken);
    }

    public static Twitter getTwitterInstance() {
        return twitter;
    }

    private class AuthURLGetter extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
                return requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                Log.e("Twitter", e.getMessage());
                return null;
            }
        }
    }

    private class AccessTokenGetter extends AsyncTask<String, Integer, AccessToken> {

        @Override
        protected AccessToken doInBackground(String... arg0) {
            try {
                return twitter.getOAuthAccessToken(requestToken, arg0[0]);
            } catch (TwitterException e) {
                Log.e("Twitter", e.getMessage());
                return null;
            }
        }
    }
}
