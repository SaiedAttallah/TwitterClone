package com.cosmos.saiedattallah.twitterclone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import twitter4j.auth.AccessToken;

public class LoginActivity extends AppCompatActivity {
    private static final int OAUTH_REQUEST = 1;
    private static final String PREFS = "prefs";
    private TwitterBackend backend;
    private SharedPreferences prefs;
    static String consumerKey;
    static String consumerSecret;
    private AccessToken accessToken;

    private void initializeVariables() {
        consumerKey = getResources().getString(R.string.consumer_key);
        consumerSecret = getResources().getString(R.string.consumer_secret);
        backend = new TwitterBackend(consumerKey, consumerSecret);
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!checkNetwork()) {
            AlertDialog dialog = createDialog();
            dialog.show();
        } else {
            initializeVariables();
            if (tokenExists()) {
                getToken();
                backend.twitterInit(accessToken);
                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            } else {
                login();
            }
        }

    }

    private boolean checkNetwork() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info == null) {
            return false;
        } return info.isConnected();
    }

    private AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No connectivity");
        builder.setMessage("Cannot connect to twitter.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        return builder.create();
    }

    private void login() {
        Intent intent = new Intent(getApplicationContext(), TwitterAuthentication.class);
        String url = backend.getAuthorizationURL();
        intent.putExtra("url", url);
        startActivityForResult(intent, OAUTH_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OAUTH_REQUEST && resultCode == RESULT_OK) {
            Uri url = Uri.parse(data.getExtras().getString("url"));
            String verifier = url.getQueryParameter("oauth_verifier");
            backend.setAccessToken(verifier);
            backend.twitterInit();
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
        accessToken = backend.getAccessToken();
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
