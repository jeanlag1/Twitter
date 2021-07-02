package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    @NonNull
    @NotNull

    Context mContext;
    List<Tweet> mTweets;
    TwitterClient mClient;

    // Views in each item for the View Holder
    ImageView mIvProfileImg;
    TextView mTvBody;
    TextView mTvScreenName;
    TextView mRelativeTimestamp;
    ImageView mIvEmbedded;
    TextView mTvUsername;
    TextView mLikeCount;
    TextView mRetweetCount;
    Button mLike;
    Button mRetweet;

    // Pass in the context and the list of tweets
    public TweetsAdapter(@NonNull Context Context, List<Tweet> tweets, TwitterClient client) {
        this.mContext = Context;
        this.mTweets = tweets;
        this.mClient = client;
    }
    @Override
    // For each row, inflate the layout
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data
        Tweet tweet = mTweets.get(position);
        // Bind the tweet with the view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mIvProfileImg = itemView.findViewById(R.id.ivProfileImg);
            mTvBody = itemView.findViewById(R.id.tvBody);
            mTvScreenName = itemView.findViewById(R.id.tvScreenName);
            mRelativeTimestamp = itemView.findViewById(R.id.tvRelativeT);
            mIvEmbedded = itemView.findViewById(R.id.ivEmbedded);
            mTvUsername = itemView.findViewById(R.id.tvUsername);
            mLikeCount = itemView.findViewById(R.id.tvLikeCount);
            mRetweetCount = itemView.findViewById(R.id.tvRetweetCount);
            mLike = itemView.findViewById(R.id.btnLike);
            mRetweet = itemView.findViewById(R.id.btnRetweet);

        }

        public void bind(Tweet tweet) {
            mTvBody.setText(tweet.mBody);
            mTvScreenName.setText("@" + tweet.mUser.mScreenName);
            mRelativeTimestamp.setText(tweet.getRelativeTimestamp());
            mTvUsername.setText(tweet.mUser.mName);
            mLikeCount.setText(String.valueOf(tweet.mLikeCount));
            mRetweetCount.setText(String.valueOf(tweet.mRetweetCount));
            Glide.with(mContext)
                    .load(tweet.mUser.mPublicImageUrl)
                    .transform(new RoundedCornersTransformation(200,0))
                    .into(mIvProfileImg);
            Glide.with(mContext)
                    .load(tweet.mEmbeddedImgUrl)
                    .transform(new FitCenter(),new RoundedCornersTransformation(60,0))
                    .into(mIvEmbedded);
            if(tweet.mLiked) {
                mLike.setBackgroundResource(R.drawable.ic_vector_heart);
            }
            if(tweet.mRetweeted) {
                mRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);
            }
            // Set even listener for like button
            onLikeClick(tweet);

            //set even listener for retweet button
            onRetweetClick(tweet);

        }
    }

    // Retweet button event listenter
    private void onRetweetClick(Tweet tweet) {
        mRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TweetAdapter", "Tweet id" + tweet.mId);
                if (!tweet.mRetweeted) {
                    mClient.publishRetweet(tweet.mId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i("TweetAdapter", "onSuccess to like tweet");
                            mRetweet.setBackgroundResource(R.drawable.ic_vector_retweet);

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("TweetAdapter", "onFailure to publish tweet", throwable);
                        }
                    });
                }
                else {
                    mClient.publishUnRetweet(tweet.mId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i("TweetAdapter", "onSuccess to like tweet");
                            mRetweet.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("TweetAdapter", "onFailure to publish tweet", throwable);
                        }
                    });
                }
            }
        });
    }

    // Like button event listenter
    private void onLikeClick(Tweet tweet) {
        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TweetAdapter", "Tweet id" + tweet.mId);
                if (!tweet.mLiked) {
                    mClient.publishLike(tweet.mId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i("TweetsAdapter", "onSuccess to like tweet");
                            mLike.setBackgroundResource(R.drawable.ic_vector_heart);

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("TweetsAdapter", "onFailure to like tweet", throwable);
                        }
                    });
                }
                else {
                    mClient.publishUnlike(tweet.mId, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i("TweetsAdapter", "onSuccess to like tweet");
                            mLike.setBackgroundResource(R.drawable.ic_vector_heart_stroke);

                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e("TweetsAdapter", "onFailure to publish tweet", throwable);
                        }
                    });
                }
            }
        });
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
