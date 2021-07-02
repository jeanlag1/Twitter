package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {
    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public String mBody;
    public String mCreatedAt;
    public User mUser;
    public String mEmbeddedImgUrl;
    public int mRetweetCount;
    public int mLikeCount;
    public boolean mLiked;
    public boolean mRetweeted;
    public String mId;

    //empty constructor for Parceler
    public Tweet(){}


    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.mLikeCount = jsonObject.getInt("favorite_count");
        tweet.mRetweetCount = jsonObject.getInt("retweet_count");
        tweet.mUser = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.mLiked = jsonObject.getBoolean("favorited");
        tweet.mRetweeted = jsonObject.getBoolean("retweeted");
        tweet.mBody = jsonObject.getString("id_str");
        if(jsonObject.has("full_text")) {
            tweet.mBody = jsonObject.getString("full_text");
        } else {
            tweet.mBody = jsonObject.getString("text");
        }
        tweet.mCreatedAt = jsonObject.getString("created_at");
        JSONObject ent = jsonObject.getJSONObject("entities");
        //check if there exists embedded images
        if (ent.has("media")) {
            tweet.mEmbeddedImgUrl = ent.getJSONArray("media")
                    .getJSONObject(0)
                    .getString("media_url_https");
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }




    public String getRelativeTimestamp() {
        String rawJsonDate = mCreatedAt;
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + "h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d";
            }
        } catch (ParseException e) {
            Log.i("PARSING", "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }
}
