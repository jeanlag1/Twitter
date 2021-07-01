package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    @NonNull
    @NotNull

    Context mContext;
    List<Tweet> mTweets;

    // Pass in the context and the list of tweets
    public TweetsAdapter(@NonNull Context mContext, List<Tweet> tweets) {
        this.mContext = mContext;
        this.mTweets = tweets;
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

        ImageView mIvProfileImg;
        TextView mTvBody;
        TextView mTvScreenName;
        TextView mRelativeTimestamp;
        ImageView mIvEmbedded;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mIvProfileImg = itemView.findViewById(R.id.ivProfileImg);
            mTvBody = itemView.findViewById(R.id.tvBody);
            mTvScreenName = itemView.findViewById(R.id.tvScreenName);
            mRelativeTimestamp = itemView.findViewById(R.id.tvRelativeT);
            mIvEmbedded = itemView.findViewById(R.id.ivEmbedded);
        }

        public void bind(Tweet tweet) {
            mTvBody.setText(tweet.mBody);
            mTvScreenName.setText(tweet.mUser.mScreenName);
            mRelativeTimestamp.setText(tweet.getRelativeTimestamp());
            Glide.with(mContext)
                    .load(tweet.mUser.mPublicImageUrl)
                    .into(mIvProfileImg);
            Glide.with(mContext)
                    .load(tweet.mEmbeddedImgUrl)
                    .into(mIvEmbedded);
//
        }
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
