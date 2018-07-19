package com.chikeandroid.tutsplus_glide;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.tasks.TasksScopes;


import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import static com.chikeandroid.tutsplus_glide.Constants.REQUEST_ACCOUNT_PICKER;
import static com.chikeandroid.tutsplus_glide.Constants.REQUEST_AUTHORIZATION;
import static com.chikeandroid.tutsplus_glide.Constants.REQUEST_GOOGLE_PLAY_SERVICES;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_ACCOUNT_NAME = "accountName";

    static final String TAG = "TasksSample";

    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;

    static final int REQUEST_AUTHORIZATION = 1;

    static final int REQUEST_ACCOUNT_PICKER = 2;

    GoogleAccountCredential credential;

    List<String> tasksList;

    ArrayAdapter<String> adapter;

    RequestQueue queue;
    int numAsyncTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button showGalleryBtn = (Button) findViewById(R.id.btn_show_gallery);
        showGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(MainActivity.this, SpaceGalleryActivity.class);
                startActivity(galleryIntent);
            }
        });

//        Button showGif = (Button) findViewById(R.id.btn_show_gif);
//        showGif.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent gifIntent = new Intent(MainActivity.this, GifsActivity.class);
//                startActivity(gifIntent);
//            }
//        });
        credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(TasksScopes.TASKS));
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential.setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        queue = Volley.newRequestQueue(this);


    }


    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog =
                        GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, MainActivity.this,
                                REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkGooglePlayServicesAvailable()) {
            haveGooglePlayServices();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                } else {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        makeRequest();
                    }
                }
                break;
        }
    }


    private void makeRequest() {
        String url = "https://photoslibrary.googleapis.com/v1/albums";
        url = Constants.URL_FILES + Constants.URL_EXTN_API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, error.getMessage());
                        error.printStackTrace();

                    }
                });
        queue.add(jsonObjectRequest);
    }


    /** Check that Google Play services APK is installed and up to date. */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }

    private void haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            chooseAccount();
        } else {
            makeRequest();
        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }
}
