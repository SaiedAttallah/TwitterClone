package com.cosmos.saiedattallah.twitterclone;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.cosmos.saiedattallah.twitterclone.models.Tweet;
import com.cosmos.saiedattallah.twitterclone.models.User;
import com.cosmos.saiedattallah.twitterclone.rest.TwitterClientProvider;
import com.facebook.stetho.Stetho;

/**
 * Created by saeed on 23/08/17.
 */

public class App extends com.activeandroid.app.Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration.Builder config = new Configuration.Builder(this);
        config.addModelClasses(Tweet.class, User.class);
        ActiveAndroid.initialize(config.create());
        Stetho.initializeWithDefaults(this);
        App.context = this;
    }

    public static TwitterClientProvider getRestClient() {
        return (TwitterClientProvider) TwitterClientProvider.getInstance(TwitterClientProvider.class, App.context);
    }
}
