package com.cosmos.saiedattallah.twitterclone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import twitter4j.auth.AccessToken;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int OAUTH_REQUEST = 1;
    private static final String PREFS = "prefs";
    static String consumerKey;
    static String consumerSecret;
    private TwitterAuthHelper twitterAuthHelper;
    private SharedPreferences prefs;
    private AccessToken accessToken;
    private Button btnLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_login:
                onClickLogin();
                break;
        }
    }

    private void initializeAuthHelper() {
        consumerKey = getResources().getString(R.string.consumer_key);
        consumerSecret = getResources().getString(R.string.consumer_secret);
        twitterAuthHelper = new TwitterAuthHelper(consumerKey, consumerSecret);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    private void onClickLogin() {
        if (!Utils.isNetworkAvailable(LoginActivity.this)) {
            showNetworkErrorDialog(getString(R.string.network_connection_error_title), getString(R.string.network_connection_error_message));
            return;
        }

        initializeAuthHelper();
        if (tokenExists()) {
            getToken();
            twitterAuthHelper.twitterInit(accessToken);
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        } else {
            this.userLogin();
        }
    }

    private void showNetworkErrorDialog(String title, String message) {
        android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, this.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, this.getString(R.string.retry),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onClickLogin();
                    }
                });
        alertDialog.show();
    }

    private void userLogin() {
        Intent intent = new Intent(getApplicationContext(), TwitterAuthActivity.class);
        String url = twitterAuthHelper.getAuthorizationURL();
        intent.putExtra("url", url);
        startActivityForResult(intent, OAUTH_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OAUTH_REQUEST && resultCode == RESULT_OK) {
            Uri url = Uri.parse(data.getExtras().getString("url"));
            String verifier = url.getQueryParameter("oauth_verifier");
            twitterAuthHelper.setAccessToken(verifier);
            twitterAuthHelper.twitterInit();
            saveToken();
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this,
                    "Cannot connect to twitter, app not authorized",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean tokenExists() {
        return prefs.contains("oauth_token") && prefs.contains("oauth_secret");
    }

    private void saveToken() {
        accessToken = twitterAuthHelper.getAccessToken();
        if (accessToken != null) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("oauth_token", accessToken.getToken());
            editor.putString("oauth_secret", accessToken.getTokenSecret());
            editor.commit();
        }
    }

    private void getToken() {
        String accessToken = prefs.getString("oauth_token", null);
        String secret = prefs.getString("oauth_secret", null);
        if (accessToken != null && secret != null) {
            this.accessToken = new AccessToken(accessToken, secret);
        }

    }
}
