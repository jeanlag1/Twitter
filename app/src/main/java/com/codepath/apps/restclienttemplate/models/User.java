package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    public String mName;
    public String mScreenName;
    public String mPublicImageUrl;

    // empty constructor for Parceler
    public User(){}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.mName = jsonObject.getString("name");
        user.mPublicImageUrl = jsonObject.getString("profile_image_url_https");
        user.mScreenName = jsonObject.getString("screen_name");
        return user;
    }
}
