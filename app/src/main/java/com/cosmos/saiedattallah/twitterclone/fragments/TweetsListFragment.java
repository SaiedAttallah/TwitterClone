package com.cosmos.saiedattallah.twitterclone.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cosmos.saiedattallah.twitterclone.App;
import com.cosmos.saiedattallah.twitterclone.EndlessRecyclerViewScrollListener;
import com.cosmos.saiedattallah.twitterclone.R;
import com.cosmos.saiedattallah.twitterclone.activities.ProfileActivity;
import com.cosmos.saiedattallah.twitterclone.activities.TweetActivity;
import com.cosmos.saiedattallah.twitterclone.adapters.TweetsAdapter;
import com.cosmos.saiedattallah.twitterclone.helpers.ItemClickSupportHelper;
import com.cosmos.saiedattallah.twitterclone.helpers.NetworkHelper;
import com.cosmos.saiedattallah.twitterclone.models.Tweet;
import com.cosmos.saiedattallah.twitterclone.rest.TwitterClientProvider;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class TweetsListFragment extends Fragment implements ComposeDialogFragment.ComposeDialogListener{

    public TweetsAdapter adapter;
    public ArrayList<Tweet> tweets;
    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.fab)
    FloatingActionButton btnFab;
    @BindView(R.id.main_fragment_content)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.design_bottom_sheet) RecyclerView rvBottomSheet;
    private TwitterClientProvider twitterClientProvider;

    private final int REQUEST_CODE_DETAIL = 40;
    //ProgressBar pb;

    abstract void populateTimeline(long maxId);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        ButterKnife.bind(this,contentView);
        setupView(contentView);
        twitterClientProvider = App.getRestClient();
        return contentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
    }

    private void setupView(View view){
        adapter = new TweetsAdapter(getActivity(), tweets);
        adapter.setViewHolderListener(new TweetsAdapter.ViewHolderListener() {
            @Override
            public void launchProfileActivity(String screenName) {
                Intent i = new Intent(getActivity(), ProfileActivity.class);

                i.putExtra("screen_name",screenName);

                startActivity(i);
            }

            @Override
            public void launchReplyDialog(String screenName) {
                ComposeDialogFragment myDialog = ComposeDialogFragment.newInstance(screenName);

                FragmentManager fm = getFragmentManager();

                myDialog.setTargetFragment(TweetsListFragment.this, 100);
                myDialog.show(fm, "fragment_compose_dialog");
            }

            @Override
            public void favoriteTweet(long tweetId) {
                if (NetworkHelper.isOnline() && NetworkHelper.isNetworkAvailable(getActivity())) {
                    twitterClientProvider.likeTweet(tweetId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.fromJSONObject(response);
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
                } else{
                    Toast.makeText(getActivity(), "You're offline, can't favorite. Check your network connection",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void unfavoriteTweet(long tweetId){
                if (NetworkHelper.isOnline() && NetworkHelper.isNetworkAvailable(getActivity())) {
                    twitterClientProvider.unlikeTweet(tweetId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.fromJSONObject(response);
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
                } else{
                    Toast.makeText(getActivity(), "You're offline, can't un-favorite. Check your network connection",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void retweetTweet(long tweetId){
                if (NetworkHelper.isOnline() && NetworkHelper.isNetworkAvailable(getActivity())) {
                    twitterClientProvider.postRetweet(tweetId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Tweet newTweet = Tweet.fromJSONObject(response);
                            add(newTweet);
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
                    Toast.makeText(getActivity(), "You're offline, can't retweet. Check your network connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        rvTweets.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvTweets.setLayoutManager(linearLayoutManager);

        ItemClickSupportHelper.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupportHelper.OnItemClickListener(){
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v){
                        launchTweetDetail(position);
                    }
                }
        );

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                int curSize = adapter.getItemCount();
                long maxId = curSize > 0 ? tweets.get(curSize - 1).getUid() : 1;
                populateTimeline(maxId);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int curSize = adapter.getItemCount();
                adapter.clear();
                adapter.notifyItemRangeRemoved(0,curSize);
                //pb.setVisibility(ProgressBar.VISIBLE);
                populateTimeline(1);
                //pb.setVisibility(ProgressBar.INVISIBLE);
                swipeContainer.setRefreshing(false);
            }
        });

        rvBottomSheet.setLayoutManager(new LinearLayoutManager(getActivity()));

        final BottomSheetBehavior behavior = BottomSheetBehavior.from(rvBottomSheet);

        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchComposeMessage(null);
            }
        });

        //pb = (ProgressBar) view.findViewById(R.id.pbLoading);
    }


    private void launchComposeMessage(String screenName){
        ComposeDialogFragment myDialog = ComposeDialogFragment.newInstance(screenName);

        FragmentManager fm = getActivity().getSupportFragmentManager();

        myDialog.setTargetFragment(TweetsListFragment.this, 100);
        myDialog.show(fm, "fragment_compose_dialog");
    }

    private void launchTweetDetail(int position){
        Intent i = new Intent(getActivity(), TweetActivity.class);

        Tweet tweet = tweets.get(position);

        i.putExtra("tweet", Parcels.wrap(tweet));

        startActivityForResult(i, REQUEST_CODE_DETAIL);
    }

    @Override
    public void onFinishedComposePeepDialog(Tweet tweet) {
        add(tweet);
    }

    public void addAll(ArrayList<Tweet> newTweets) {
        int curSize = adapter.getItemCount();
        tweets.addAll(newTweets);
        adapter.notifyItemRangeInserted(curSize, newTweets.size() - 1);
    }

    public void add(Tweet newTweet){
        tweets.add(0, newTweet);
        adapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

}
