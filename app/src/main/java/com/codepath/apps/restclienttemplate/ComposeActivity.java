package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LEN = 140;
    public static final String TAG = "ComposeActivity";

    EditText mEtCompose;
    Button mBtnTweet;
    TwitterClient mClient;
    TextInputLayout mTxtInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        mClient = TwitterApp.getRestClient(this);
        mEtCompose = findViewById(R.id.etCompose);
        mBtnTweet = findViewById(R.id.btnTweet);
        mTxtInput = findViewById(R.id.tILayout);

        //Set click listener on button
        mBtnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = mEtCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LEN) {
                    Toast.makeText(ComposeActivity.this, "Sorry, your tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }
//                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();

                // Publish the tweet
                publish(tweetContent);
            }
        });

        // Add Character Max Length to TextInputLayout
        mTxtInput.setCounterMaxLength(MAX_TWEET_LEN);
    }

    // Make an API call to Twitter to publish the tweet
    private void publish(String tweetContent) {
        mClient.publishTweet(tweetContent, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSucces to publish tweet");
                try {
                    Tweet tweet = Tweet.fromJson(json.jsonObject);
                    Log.i(TAG, "Published tweet says: " + tweet.mBody);
                    Intent i = new Intent();
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    // set result code and bundle data for response
                    setResult(RESULT_OK, i);
                    //closes the activity, pass data to parent
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure to publish tweeet", throwable);
            }
        });
    }
}