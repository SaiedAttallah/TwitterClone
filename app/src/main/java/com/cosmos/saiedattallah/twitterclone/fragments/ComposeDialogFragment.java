package com.cosmos.saiedattallah.twitterclone.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmos.saiedattallah.twitterclone.App;
import com.cosmos.saiedattallah.twitterclone.helpers.NetworkHelper;
import com.cosmos.saiedattallah.twitterclone.R;
import com.cosmos.saiedattallah.twitterclone.models.Tweet;
import com.cosmos.saiedattallah.twitterclone.rest.TwitterClientProvider;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComposeDialogFragment extends BottomSheetDialogFragment {
    private TwitterClientProvider twitterClientProvider;
    @BindView(R.id.tvLetterCount)
    TextView tvLetterCount;
    @BindView(R.id.etPeepBody)
    EditText etBody;
    @BindView(R.id.btnPeep)
    Button btnPeep;
    @BindView(R.id.ibtnCancel)
    ImageButton ibtnCancel;
    private Tweet tweet;
    private final static int MAX_CHAR_COUNT = 140;
    String screenName;

    public static ComposeDialogFragment newInstance(String screenName){
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", screenName);
        ComposeDialogFragment fragment = new ComposeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        screenName = getArguments().getString("screen_name");
    }


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public interface ComposeDialogListener {
        void onFinishedComposePeepDialog(Tweet tweet);
    }

    @Override
    public void setupDialog(Dialog dialog, int style){
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_compose_dialog, null);
        dialog.setContentView(contentView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        ButterKnife.bind(this,contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        twitterClientProvider = App.getRestClient();
        setupViews();
    }

    private void setupViews(){
        tvLetterCount.setText(Integer.toString(MAX_CHAR_COUNT));
        etBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int curLength = etBody.getText().length();
                tvLetterCount.setText(Integer.toString(MAX_CHAR_COUNT - curLength));
            }
        });
        if (!TextUtils.isEmpty(screenName)) {
            final SpannableStringBuilder boldedScreenName = new SpannableStringBuilder(screenName);
            boldedScreenName.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, screenName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            etBody.setText(boldedScreenName + " ");
        }
        btnPeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePeep(view);
            }
        });
        ibtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        etBody.requestFocus();
    }

    public void savePeep(View view){
        RequestParams params = new RequestParams();
        params.put("status",etBody.getText());

        if (NetworkHelper.isOnline() && NetworkHelper.isNetworkAvailable(getContext())) {
            twitterClientProvider.postStatusUpdate(params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweet = Tweet.fromJSONObject(response);
                    ComposeDialogListener listener = (ComposeDialogListener) getTargetFragment();
                    listener.onFinishedComposePeepDialog(tweet);
                    dismiss();
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
            Toast.makeText(getActivity(), "Check your network connection",Toast.LENGTH_LONG).show();
        }
    }

}
